package redes.routing.core

import groovy.transform.ThreadInterrupt

import java.net.DatagramPacket
import java.net.DatagramSocket

import java.util.ArrayList
import java.util.Arrays
import java.lang.Thread


import redes.routing.core.datastructure.Table

import redes.routing.ui.ANSI

import library.Protocol
import library.HTTP
import library.JSON

class SocketModule {

	private DatagramSocket 	socket
	private Firmware	 	firmware
	private boolean	 		enabled
	private boolean	 		updated
	private Thread 			watch

	private Integer			wired
	
	private final Integer	port

	SocketModule (int port) throws SocketException {
		this.socket = new DatagramSocket(port)
		this.firmware = Firmware.getInstance()
		this.updated = false
		this.wired = null
		this.port = port
		
		firmware.reroute(port, 0, -1)
		start() // <- watch, enabled
	}

	def getPort() { port }
	def getWire() { wired }
	def getAlive() { updated }
	def resetAlive() { updated = false	}

	/* CONTROL INTERFACE ------------------------------*/


	def start() {
		firmware.reroute(port, 0, -1)
		watch = new Thread(watchdog)
		watch.start()
	}

	def stop() {
		enabled = false
		watch.stop()
	}

	def kill() {
		firmware.reroute(port)
		stop()
		socket.close()
	}

	// Internally call
	def unwire() {
		if(this.wired) {
			reply(JSON.parse(['action':'unwire', 'src':this.port, 'dst':this.wired]), this.wired)
			firmware.reroute(wired, 16, this.wired, this.port)
			this.wired = null
		}
	}

	// Request sent by another router
	def unwire(int wire, DatagramPacket packet) {
		def httpCode = Protocol.packetStatus(packet)
		if(httpCode != HTTP.OK.toString()) {
			def header = JSON.parse(['action':'unwire', 'src':this.port, 'dst':wire])
			if(wire == wired) {
				firmware.reroute(wire, 16, this.wired, this.port)
				wired = null
				reply("${header}${status(HTTP.OK)}", wire)
			} else {
				reply("${header}${status(HTTP.UNAUTHORIZED)}", wire)
			}
		}
	}

	// Internal call
	def send(String message) {
		if(!wired)
			throw new NullPointerException
			("There is no wired connection on this module.")
		try {
			if(enabled)
				socket.send(
					createPacket(
						JSON.parse(['action':'message', 'content':message, 'src':port]),
						wired
					)
				)
		}
		catch (Exception e)	{ println e.getLocalizedMessage() }
	}

	// Request reply
	private reply(String message, int wire = wired) {
		if(!wire)
			throw new NullPointerException
			("There is no wired connection on this module.")
		try { if(enabled) socket.send( createPacket(message, wire) ) }
		catch (Exception e)	{ println e.getLocalizedMessage() }
	}

	def wire(int wire, DatagramPacket packet = null) throws IllegalAccessError {
		def header = JSON.parse(['action':'wire', 'src':this.port, 'dst':wire, 'origin':this.port])
		try{
			if(wired) {
				if(wired == wire) {
					def headerMap = Protocol.packetHeader(packet)
					def httpCode  = Protocol.packetStatus(packet)

					if(httpCode == HTTP.NOT_ACCEPTABLE.toString()) {
						wired = null
						return
					} 

					if(httpCode != HTTP.OK.toString())
						reply("${header}${status(HTTP.OK)}", wire)

					firmware.reroute(headerMap.get("origin") as int, 1, (headerMap.get("src") as int), this.port)
					return
				} else {
					reply("${header}${status(HTTP.NOT_ACCEPTABLE)}", wire)
					return
				}
			}

			this.wired = wire
			reply(header)
		}catch(Exception e){e.printStackTrace()}
	}


	/* CONNECTION HANDLER -----------------------------*/

	// Handle received messages
	def message (int wire, DatagramPacket packet = null) {
		def msg = Protocol.packetHeader(packet)
		println "received ${this.port} msg: ${msg.get("content")}"
	}

	// Handle received files
	def file (int wire, DatagramPacket packet = null) {
		def msg = Protocol.packetHeader(packet)
		println "received ${this.port} file: ${msg.get("content")}"
	}

	// Handle called rips by UDP
	def rip(int wire, DatagramPacket packet = null) {
		if(wired)
			if(!packet) {
				def table = firmware.getReplyTable()
				def map = [:]
				table.each { redirect ->
					map.put(redirect[0], redirect[1])
				}
				def header = JSON.parse(['action':'rip', 'src':this.port, 'table':map])
				try { reply(header) }
				catch(Exception e) {}
			} else {
				def map = Protocol.packetHeader(packet)
				def src = map.get("src") as int
				if(src == wired) {
					def table = JSON.convertMap(map.get("table"))
					if(table)
						firmware.routesUpdate(map.get("src") as int, table)
					else
						println "There is no table!"
				}
			}
		}

	/* CONNECTION INTERFACE ---------------------------*/

	/* Format to Status code to be able to decompile */
	private String status(HTTP http) { "%${http}%" }

	/* Create a packet to be sent to the server */
	private DatagramPacket createPacket (String data, Integer wire = wired)
	{ createPacket( data.getBytes(), wire ) }
	private DatagramPacket createPacket (byte[] data, Integer wire = wired) {
		new DatagramPacket (
			data,
			data.length,
			Firmware.domain,
			wire
		)
	}

	/* Cut the unused response size */
	private String trim(DatagramPacket packet) {
		return new String(
				Arrays.copyOf(
					packet.getData(),
					packet.getLength()
			)
		);
	}

	/* Clone the packet to be able tochange the state and resend it */
	private DatagramPacket clonePacket(DatagramPacket packet) {
		new DatagramPacket (
			packet.getData(),
			packet.getLength(),
			packet.getAddress(),
			packet.getPort()
		)
	}

	// Generic method dispatcher 
	private void handler(DatagramPacket packet) {
		updated = true
		def map = Protocol.packetHeader(packet)
		if(map.get("action") != "rip")
			println "${ANSI.GREEN}[MODULE] ${this.port}${ANSI.RESET} <- ${trim(packet)}"

		try {
			"${map.get('action')}"(Integer.parseInt(map.get("src")), packet)
		} catch(Exception e) { println e.getLocalizedMessage() + " {There is no action or src on header}" }
	}

	/* Watch the port waiting for request */
	private Runnable watchdog = new Runnable() {
		public void run() {
			enabled = true
			byte[] data = new byte[1024]
			DatagramPacket packet =
				new DatagramPacket(data, data.length);
			while(enabled) {
				try {
					socket.receive(packet)
					handler(clonePacket(packet))
					Arrays.fill(data, (byte) 0)
				} catch (Exception e) { e.printStackTrace() }
			}
		}
	}

	@Override
	public String toString() {
		"{ \"status\": \"${enabled?"up":"down"}\"${wired?", \"wired\": \"$wired\"":""} }"
	}
	
}
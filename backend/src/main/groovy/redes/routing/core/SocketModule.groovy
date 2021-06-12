package redes.routing.core

import groovy.transform.ThreadInterrupt

import java.net.DatagramPacket
import java.net.DatagramSocket

import java.util.Arrays
import java.lang.Thread


import redes.routing.ui.ANSI

import library.Protocol
import library.HTTP
import library.JSON

// @ThreadInterrupt
class SocketModule {
	
	private DatagramSocket 	socket
	private Firmware	 	firmware
	private boolean	 		enabled
	private Thread 			watch

	private Integer			wired
	
	private final Integer	port

	SocketModule (int port) throws SocketException {
		this.socket = new DatagramSocket(port)
		this.firmware = Firmware.getInstance()
		this.wired = null
		this.port = port
		start() // <- watch, enabled
	}



	/* CONTROL INTERFACE ------------------------------*/


	def start() {
		watch = new Thread(watchdog)
		watch.start()
	}

	def stop() {
		enabled = false
		watch.stop()
	}

	def kill() {
		stop()
		socket.close()
	}

	// Internally call
	def unwire() {
		if(this.wired){
			reply(JSON.parse(['action':'unwire', 'src':this.port, 'dst':this.wired]), this.wired)
			this.wired = null
		}
	}

	// Request sent by another router
	def unwire(int wire, DatagramPacket packet) {
		def httpCode = Protocol.packetStatus(packet)
		if(httpCode != HTTP.OK.toString()) {
			def header = JSON.parse(['action':'unwire', 'src':this.port, 'dst':wire])
			if(wire == wired) {
				wired = null
				reply("${header}${status(HTTP.OK)}", wire)
			} else {
				reply("${header}${status(HTTP.UNAUTHORIZED)}", wire)
			}
		}
	}

	def send(String message) {
		if(!wired)
			throw new NullPointerException
			("There is no wired connection on this module.")
		try { 
			socket.send(
				createPacket(
					JSON.parse(['action':'message', 'content':message]),
					wired
				)
			)
		}
		catch (Exception e)	{ println e.getLocalizedMessage() }
	}

	def reply(String message, int wire = wired) {
		if(!wire)
			throw new NullPointerException
			("There is no wired connection on this module.")
		try { socket.send( createPacket(message, wire) ) }
		catch (Exception e)	{ println e.getLocalizedMessage() }
	}

	def wire(int wire, DatagramPacket packet = null) throws IllegalAccessError {
		def header = JSON.parse(['action':'wire', 'src':this.port, 'dst':wire])
		if(wired) {
			if(wired == wire){
				def httpCode = Protocol.packetStatus(packet)

				if(httpCode == HTTP.NOT_ACCEPTABLE.toString()) {
					wired = null
					return
				} 

				if(httpCode != HTTP.OK.toString())
					reply("${header}${status(HTTP.OK)}", wire)

				firmware.reroute(wire, 0, this.port)
				return
			} else {
				reply("${header}${status(HTTP.NOT_ACCEPTABLE)}", wire)
				return
			}
		}

		this.wired = wire
		reply(header)
	}


	/* CONNECTION HANDLER -----------------------------*/

	def message (int wire, DatagramPacket packet = null) {
		def msg = Protocol.packetMessage(packet)
		println "received on ${this.port}: ${msg}"
	}

	/* CONNECTION INTERFACE ---------------------------*/

	/* Format to Status code to be able to decompile */
	private String status(HTTP http) { "%${http}%" }

	/* Create a packet to be sent to the server */
	private DatagramPacket createPacket (String data, Integer wire = wired)
	{ return createPacket( data.getBytes(), wire ) }
	private DatagramPacket createPacket (byte[] data, Integer wire = wired) {
		new DatagramPacket(
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


	private void handler(DatagramPacket packet) {
		//TODO Print filter
		println "${ANSI.GREEN}[MODULE] ${this.port}${ANSI.RESET} <- ${trim(packet)}"

		def map = Protocol.packetHeader(packet)
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
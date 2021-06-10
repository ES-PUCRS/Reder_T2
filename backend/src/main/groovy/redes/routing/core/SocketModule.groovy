package redes.routing.core

import groovy.transform.ThreadInterrupt

import java.net.DatagramPacket
import java.net.DatagramSocket

import java.util.Arrays
import java.lang.Thread

@ThreadInterrupt
class SocketModule {
	
	DatagramSocket 	socket
	boolean	 		enabled
	Thread 			watch

	Integer			wired

	SocketModule (int port) throws SocketException {
		socket = new DatagramSocket(port)
		start();
	}



	def send(String message) {
		try {
			socket.send( createPacket(message) )
		} catch (Exception e)
		{ println e.getLocalizedMessage() }
	}



	def wire(int wire) {
		wired = wire
	}

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



	/* Create a packet to be sent to the server */
	private DatagramPacket createPacket (String data)
	{ return createPacket( data.getBytes()) }
	private DatagramPacket createPacket (byte[] data) {
		return new
			DatagramPacket(
				data,
				data.length,
				Firmware.domain,
				wired
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

	private Runnable watchdog = new Runnable() {
		public void run() {
			enabled = true
			byte[] data = new byte[1024]
			DatagramPacket receivedData =
				new DatagramPacket(data, data.length);
			while(enabled) {
				try {
					socket.receive(receivedData);
					println("Data port> ${trim(receivedData)}")
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
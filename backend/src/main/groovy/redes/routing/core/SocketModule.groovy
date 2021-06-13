package redes.routing.core

import java.lang.Thread

class SocketModule {
	
	DatagramSocket socket
	boolean threadAlive
	Thread watch

	SocketModule (int port) throws SocketException {
		socket = new DatagramSocket(port)
		start();
	}

	def start() {
		watch = new Thread(watchdog)
		watch.start()
	}

	def stop() {
		threadAlive = false
		watch.stop()
	}

	private Runnable watchdog = new Runnable() {

		@Override
		public void run() {
			threadAlive = true
			while(threadAlive){
				watch.sleep(500)
			}
		}

		public void stop() { threadAlive = false }
	}

	@Override
	public String toString() {
		"{ \"status\": \"${threadAlive?"up":"down"}\" }"
	}
	
}
package redes.routing.core

import java.lang.Thread

class SocketModule {

	private Runnable watch = new Runnable() {
		boolean threadAlive

		public void run() {
			while(threadAlive){
				
			}
		}

		public void stop() { threadAlive = false }
	}
	
}
package redes.routing.io

import redes.routing.core.Firmware
import redes.routing.ui.ANSI

class HardDrive {

	def static readFile (Firmware firmware, String _file) {
		def file
		try{
			file = new Object() { }.getClass().getResource("/HardDisk/${_file}")
		}catch(Exception e) {
			println "${ANSI.RED_BOLD} Error reading file: ${ANSI.RED_UNDERLINE} ${_file} ${ANSI.RESET}"
			println "${ANSI.WHITE}" 			+
					"${ANSI.RED_BACKGROUND} " 	+ 
						"${e.getMessage()}"		+
					"${ANSI.RESET}"
		}
		// if(file)
			// return decodeAssembly(cpu, file) //?.text
	}

}
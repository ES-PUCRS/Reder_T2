package redes.routing.io

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.io.File

import redes.routing.core.Firmware
import redes.routing.ui.ANSI

import redes.routing.Router

class HardDrive {

	def static readFile (String _file) {
		def file
		try{
			file = new Object() { }.getClass().getResource("/HardDisk/${_file}")
		} catch(Exception e) {
			println "${ANSI.RED_BOLD} Error reading file: ${ANSI.RED_UNDERLINE} ${_file} ${ANSI.RESET}"
			println "${ANSI.WHITE}" 			+
					"${ANSI.RED_BACKGROUND} " 	+ 
						"${e.getMessage()}"		+
					"${ANSI.RESET}"
		}
		file
	}


	def static writeFile(String filename, String _file) {
		try {
			Files.write(Paths.get("${Router.properties."hardisk.path"}/${filename}"), toArray(_file))
		} catch(Exception e) {
			println "${ANSI.RED_BOLD} Error writing file: ${ANSI.RED_UNDERLINE} ${_file} ${ANSI.RESET}"
			println "${ANSI.WHITE}" 			+
					"${ANSI.RED_BACKGROUND} " 	+ 
						"${e.getMessage()}"		+
					"${ANSI.RESET}"
		}
	}


   /*
    *   As the data is transported by strings on the header
    *   there is needed this method to convert the data string
    *   into the original byte array values.
    *
    *   There may be an exception if the sender does not convert
    *   to byte before send because the String need to be casted
    *   to int before became a byte array.
    *   Therefore, is known that this exception can happen on the
    *   conversion.
    */
   private static byte[] toArray(String str) {
      try{
         int[] list =
            Arrays.stream(
                  str.replaceAll("\\[|\\]","")
                  .split(", ")
            )
               .map(Integer::valueOf)
               .mapToInt(i -> i)
               .toArray();

         byte[] array = new byte[list.length];
         for (int i = 0; i < list.length; i++)
            array[i] = (byte) list[i];
         return array;
      } catch(Exception e) {System.out.println(e);}

      return null;
   }

}
package library

import static java.util.stream.Collectors.joining;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.net.DatagramPacket

class Protocol {

    /*
     *   Regex due to decompile the message to be sent
     */
    public static String packetStatus(DatagramPacket packet) {
   		if (!packet) return HTTP.NO_CONTENT.toString()
    	String sentence = new String(packet.getData())

   		Matcher matcher
				= Pattern
					.compile("(%\\[\\d{3}: [A-Za-z _]*\\]%)")
					.matcher(sentence)
		
   		if(matcher.find())
			return matcher.group(0).replaceAll("%","")
   		return HTTP.NO_CONTENT.toString()
    }


    /*
     *   Regex due to decompile the message to be sent
     */
    public static String packetMessage(DatagramPacket packet = null)
    { return packetMessage(new String(packet.getData())) }
    public static String packetMessage(String sentence) {
   		Matcher matcher
				= Pattern
					.compile("(\".*\")")
					.matcher(sentence)
		
   		if(matcher.find())
			return matcher.group(0)
   		return HTTP.NO_CONTENT.toString()
    }

   	/*
     *   Regex due to decompile the client message header due to
     *   provide more informations to control the data flow
     *
     *   return a map with all parameters given on the header
     */
   	public static Map<String, String> packetHeader(DatagramPacket packet) {
     	Map<String, String> map = new HashMap<String, String>()
     	String sentenceMap, sentence;
     	try{
     	  	sentenceMap = sentence = new String(packet.getData())

     	  	Matcher arrays
     	  	   	= Pattern
     	  	   		.compile("(\\[.*?\\])")
     	  	   		.matcher(sentence)
     	  	for (int i = 0; arrays.find(); i++) {
     	  		String array = sentence.substring(arrays.start(), arrays.end())
     	  		sentenceMap = sentenceMap.replace(array,"^"+i)
     	  		map.put("^"+i,array)
     	  	}

     	  	Matcher matcher
     	  		= Pattern
     	  			.compile("(\\{.*[^\\]]\\})")
     	  			.matcher(sentenceMap)

     	  	if(matcher.find()){
     	  	   	Map<String, String> res =
     	  			Arrays.stream(
     	  			   	matcher.group(0)
     	  			           .replaceAll("\\{|\\}","")
     	  			           .split(", ")
     	  			)
     	  				.map(string -> string.replaceAll("\\s","").split(":"))
     	  				.collect(
     	  					Collectors.toMap(
     	  						array -> array[0].replaceAll("\"",""),
     	  						array -> array[1].replaceAll("\"","")
     	  					)
     	  				);

     	  		if(map.size() > 0) {
     	  			for (Map.Entry<String, String> entry : res.entrySet()) {
     	  				if(entry.getValue().contains("^"))
 	  						res.replace(entry.getKey(), map.get(entry.getValue()))
     	  			}
     	  		}
     	  		return res
     	  	}
     	} catch (Exception e) { e.printStackTrace() }
     	null
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
   	public static byte[] toArray(String str) {
   	   	try{
   	   	   	int[] list =
   	   	   	   	Arrays.stream(
   	   	   	     	str.replaceAll("\\[|\\]","")
   	   	   	     	.split(", ")
   	   	   	   	)
   	   	   	   .map(Integer::valueOf)
   	   	   	   .mapToInt(i -> i)
   	   	   	   .toArray()
			
   	   	   	byte[] array = new byte[list.length]
   	   	   	for (int i = 0; i < list.length; i++)
   	   	   		array[i] = (byte) list[i]
   	   	   	return array;
   	   	} catch(Exception e) { println(e) }
		
   	   	null
   	}


	/*
	*   Since the connection need to send different packages
	*   due to manage the size of the file, this method is used
	*   to put all together as a String array = "[0, 0, 0]"
	*/
	public static String merge(Map<Integer, String> ackBook) {
 	   	"["+
   	   		ackBook.entrySet()
       			.stream()
       			.map(e -> e.getValue().replaceAll("\\[|\\]",""))
       			.collect(joining(", "))
 	   	+"]"
	}
}
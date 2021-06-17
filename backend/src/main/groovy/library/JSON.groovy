package library

import java.util.IllegalFormatException
import java.util.stream.Collectors;

class JSON {

	def static parse(Map map) {
		def json = ""
		map.each { key, value ->
			if(!value.toString().startsWith('{'))
				value = "\"${value}\""
    		json += ", \"${key}\": ${value}"
		}

		"{${json.substring(1)} }"
	}

	def static parse(String key, String value) {
		if(!value.toString().startsWith('{'))
			value = "\"${value}\""
		"{ \"${key}\": ${value} }"
	}


	def static append(String json, Map map)
				throws IllegalFormatException {
		json = verify(json)
		"${json.substring(0, json.length() - 2)},${parce(map).substring(1)}"
	}


	def static append(String json, String key, String value)
								throws IllegalFormatException {
		json = verify(json) 
		"${json.substring(0, json.length() - 2)},${parse(key, value).substring(1)}"
	}


	// TODO
	def static verify(String json) {
		if(false)
			throw new IllegalFormatException("The json is incorrectly formated and can not be auto fixed")

		json
	}


   	/*
   	 *   TODO
   	 */
   	public static Map convertMap(String str) {
   	   	if(str.charAt(0) != '[' && str.charAt(str.length() - 1) != ']')
   	   		throw new IllegalArgumentException("Invalid format")
   	   	try{
   	   	   	Arrays.stream(
   	   	     	str.replaceAll("\\[|\\]","")
   	   	     	.split(", ")
   	   	   	)
   	   	   	.map(string -> string.replaceAll("\\s","").split(":"))
			.collect(
				Collectors.toMap(
					array -> array[0],
					array -> array[1]
				)
			)
   	   	} catch(Exception e) { println(e.getLocalizedMessage()) }
   	}

}
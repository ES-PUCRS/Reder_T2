package library

import java.util.IllegalFormatException

class JSON {


	// TODO
	def static parse(Map map) {
		"{ \"${map.toString()}\" }"
	}


	def static parse(String key, String value) {
		"{ \"${key}\": \"${value}\" }"
	}


	def static append(String json, Map map)
				throws IllegalFormatException {
		json = verify(json)
		json.replaceAll(" }", ",${parce(map).substring(1)}")
	}


	def static append(String json, String key, String value)
								throws IllegalFormatException {
		json = verify(json) 
		json.replaceAll(" }", ",${parse(key, value).substring(1)}")
	}


	// TODO
	def static verify(String json) {
		if(false)
			throw new IllegalFormatException("The json is incorrectly formated and can not be auto fixed")
		json
	}


}
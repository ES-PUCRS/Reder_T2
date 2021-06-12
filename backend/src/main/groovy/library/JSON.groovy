package library

import java.util.IllegalFormatException

class JSON {

	def static parse(Map map) {
		def json = ""
		map.each { key, value ->
    		json += ", \"${key}\": \"${value}\""
		}

		"{${json.substring(1)} }"
	}

	def static parse(String key, String value) {
		"{ \"${key}\": \"${value}\" }"
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

}
package redes.routing.core

import java.net.HttpURLConnection
import java.net.URL

import java.io.InputStreamReader
import java.lang.StringBuilder
import java.io.BufferedReader

import library.Protocol

class RouterConnection {

	static def requestModule(int request) {
		try {
			HttpURLConnection conn = setConnection(request, 'connect')

			if(conn.getResponseMessage() == 'OK') {
				def reader = new BufferedReader(new InputStreamReader((conn.getInputStream())))
				def builder = new StringBuilder()
				String output

				while ((output = reader.readLine()) != null) {
					builder.append(output)
				}

				return Protocol.packetHeader(builder.toString())
			}
			
			null
		} catch(Exception e) { e.printStackTrace() }
	}



	static def setConnection(int target, String uri, Map params = null, String method = "GET") {
		URL url = new URL("http://localhost:${target}/API/${uri}${defParams(params)}")
		HttpURLConnection conn = (HttpURLConnection) url.openConnection()
		conn.setRequestMethod(method)
		conn
	}

	static def defParams(Map map){
		def params = ""
		if(map)
			map.each { key, value ->
	    		res += "&\"${key}\"=\"${value}\""
			}

		if(params)
			params = "?${params.substring(1)}"

		params
	}

}
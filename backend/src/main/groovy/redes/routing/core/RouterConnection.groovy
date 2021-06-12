package redes.routing.core

import java.net.HttpURLConnection
import java.net.URL

import java.io.InputStreamReader
import java.lang.StringBuilder
import java.io.BufferedReader

import library.Protocol

class RouterConnection {

	static def requestModule(int request) {
		HttpURLConnection conn = setConnection(request, 'connect')

		if(conn.getResponseMessage == 'OK') {
			def reader = new BufferedReader(new InputStreamReader((conn.getInputStream())))
			def builder = new StringBuilder()
			String output

			while ((output = reader.readLine()) != null) {
				builder.append(output)
			}
			def str = builder.toString()

			def map = Protocol.packetHeader(str)
			println map
		}
	}

	static def setConnection(int target, String uri, def params = null) {
		URL url = new URL("http://localhost:${target}/API/${uri}${defParams(params)}")
		HttpURLConnection conn = (HttpURLConnection) url.openConnection()
		conn.setRequestMethod("GET")
		conn
	}

	static def defParams(def params){
		if(!params){
			""
		} else {
			"?"
		}
	}

}
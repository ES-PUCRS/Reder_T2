package redes.routing.ui.server

import groovy.text.SimpleTemplateEngine 
import groovy.lang.Lazy

import redes.routing.Router

class Render{

	@Lazy
	private static Properties properties

	def static index(def map) {	null }
	// def static console(def map) {
	// 	def file = new File(root, "console.html")
	// 	def writeList 		= ""
	// 	def readList 		= ""
	// 	def response		= ""
		
	// 	if(!map.isEmpty())
	// 		map.each{ key, value -> 
	// 			response += key
	// 		}

	// 	def disabled
	// 	pm.filterByIORequest(IOREQUEST.READ).each { block ->
	// 		if(block.getProcessName().equals(response))
	// 			 disabled = "disabled"
	// 		else disabled = ""
	// 		readList +=
	// 		"""
	// 			<form id="readForm" action="/unblock">
	// 				<label for="block.getProcessName()">${block.getProcessName()}</label>
	// 				<br>
	// 				<input type="number" id="${block.getProcessName()}" name="${block.getProcessName()}" ${disabled}>
	// 				<br><br>
	// 			</form>
	// 		"""
	// 	}
	// 	if(readList.equals(""))
	// 		readList = "<p>There is no blocked process waiting to be read</p>"

	// 	def disabledField
	// 	pm.filterByIORequest(IOREQUEST.WRITE).each { block ->
	// 		if(block.getProcessName().equals(response)){
	// 			disabledField = "disabled"
	// 			disabled = "disabled"
	// 		} else {
	// 			disabled = "readonly"
	// 			disabledField = ""
	// 		}
	// 		writeList +=
	// 		"""
	// 			<form id="writeForm" action="/unblock">
	// 				<label for="block.getProcessName()">${block.getProcessName()}</label>
	// 				<br>
	// 				<input class="textField" type="text" id="${block.getProcessName()}" name="${block.getProcessName()}" value="${block.getIoRegisters()[1]}" ${disabled}>
	// 				<input class="textFieldButton" type="submit" value="Free" ${disabledField}>
	// 				<br><br>
	// 			</form>
	// 		"""
	// 	}
	// 	if(writeList.equals(""))
	// 		writeList = "<p>There is no blocked process waiting to be written</p>"

		
	// 	def binding = [
	// 		'writeList' : writeList,
	// 		'readList': readList
	// 	]
	// 	new SimpleTemplateEngine()
	// 		.createTemplate(file)
	// 		.make(binding)
	// }


	def static importProperties(){
		new Object() {}
	    	.getClass()
	    	.getResource( Router.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	}
}

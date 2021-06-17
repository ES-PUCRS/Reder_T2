package redes.routing.core.datastructure

import java.util.stream.Collectors


import java.util.stream.Stream

import java.util.Collection
import java.util.ArrayList
import java.util.Arrays
import java.util.List

import java.util.Random

class Table {
	
	private Map<Integer, Integer> destinationKey
	private Map<Integer, ArrayList<Integer>> hopKey
	private Map<Integer, Integer[]> wrapper
	
	private static final int destination = 0
	private static final int metric = 1
	private static final int hop = 2

	public Table() {
		destinationKey = new HashMap()
		hopKey = new HashMap()
		wrapper = new HashMap()
	}


	def get(int destination) {
		try {
			wrapper.get(destinationKey.get(destination))
		} catch(Exception e) { e.printStackTrace() }
	}

	def add(int destination, int metric, int hop) {
		try{
			def key = destinationKey.get(destination)
			ArrayList arr

			if(key) {
				arr = wrapper.get(key)
				arr.add(this.destination, destination)
				arr.add(this.metric, metric)
				arr.add(this.hop, hop)
			} else {
				key = generateKey()
				destinationKey.put(destination, key)

				if(hopKey.containsKey(hop)) {
					arr = hopKey.get(hop)
					arr.add(key)
				} else {
					hopKey.put(hop, Arrays.asList(key) as ArrayList)
				}

				println "------------BEFORE-------------"
				println	wrapper
				println key
				println	wrapper.get(key)
					// if(!wrapper.replace(key, [destination, metric, hop]))
					wrapper.put(key, [destination, metric, hop])
				println	wrapper.get(key)
				println	wrapper
				println "------------AFTER-------------"
				
			}
		}catch(Exception e) {e.printStackTrace()}
	}

	def sync(int nextHop, ArrayList table, ArrayList modules) {
		def origin = arrayByHop(nextHop)
		
		// println wrapper
		println "------------------------------------------ run"

		def local = new ArrayList(wrapper?.values())
							.stream()
							.filter(x ->
								!modules.contains(x[this.destination])
							)
							.collect(
							  	Collectors.toMap(
			                        array -> array[this.destination],
			                        array -> array
		                     	)
	                     	)

		def remote = table.stream()
						  .filter(x ->
						  	!modules.contains(x[this.destination])
						  )
						  .collect()

		def remoteMap = remote.stream()
							  .collect(
							    	Collectors.toMap(
				                        array -> array[this.destination],
				                        array -> array
			                      	)
		                      )

		def missing = origin.stream()
							.filter(x ->
								!remoteMap.containsKey(x[this.destination])
							)

		remote.each { row ->
			def localTable = local.get(row[this.destination])
			if(!localTable || row[this.metric] < localTable[this.metric])
				add(row[this.destination], row[this.metric] + 1, nextHop)
		}

		missing.each { redirect ->
			println "REMOVE:: ${redirect}"
		}

	}

	def arrayByHop(int dst) {
		wrapper?.values()
				.stream()
				.filter(x ->
					dst == x[this.hop]
				)
				.collect()
	}

	def contains(int destination) {
		destinationKey.containsKey(destination)
	}

	def remove(int destination) {
		try {
			println destinationKey
			def key = destinationKey.get(destination) 
			def hop = wrapper.get(key)[this.hop]
			ArrayList arr = hopKey.get(hop) as ArrayList
			arr.remove(arr.indexOf(key))

			destinationKey.remove(destination)
			wrapper.remove(key)
		} catch(Exception e) {e.printStackTrace()}
	}

	def getList(){
		new ArrayList(wrapper?.values())
	}

	def getReplyList() {
		wrapper?.values()
				.stream()
		        .flatMap(entry ->
		        	Stream.of(
		        		entry, new int[] { entry[0], entry[1] }
	        		)
	        	)
				.filter(x -> x.size() < 3)
		        .toArray()
	}

	def generateKey() {
		int key = 0
		
		do {
			key = new Random().nextInt(11711)
		} while (wrapper.containsKey(key))

		key
	}

	@Override
	public String toString() {
		def line = ""
		
		wrapper.each{ key, value ->
			line += "[${value[0]}, ${value[1]}, ${value[2]}], "
		}

		if(line)
			line = line?.substring(0, line.length()-2)

		line
	}

}
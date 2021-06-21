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

	private Map<Integer, Integer> forwarding
	private Map<Integer, Integer> reverse
	
	private static final int destination = 0
	private static final int metric = 1
	private static final int hop = 2

	public Table() {
		destinationKey = new HashMap()
		hopKey = new HashMap()
		wrapper = new HashMap()

		forwarding = new HashMap()
		reverse = new HashMap()
	}


	def get(int destination) {
		try {
			wrapper.get(destinationKey.get(destination))
		} catch(Exception e) { e.printStackTrace() }
	}

	def contains(int destination) {
		destinationKey.containsKey(destination)
	}

	def add(int destination, int metric, int hop) {
		try{
			def key = destinationKey.get(destination)
			ArrayList arr

			if(key) {
				arr = wrapper.get(key)
				
				if(arr) {
					arr.set(this.destination, destination)
					arr.set(this.metric, metric)
					arr.set(this.hop, hop)
				} else {
					wrapper.put(key, [destination, metric, hop])
				}
			} else {
				key = generateKey()
				destinationKey.put(destination, key)

				if(hopKey.containsKey(hop)) {
					arr = hopKey.get(hop)
					arr.add(key)
				} else {
					hopKey.put(hop, Arrays.asList(key) as ArrayList)
				}

				wrapper.put(key, [destination, metric, hop])
			}
		} catch(Exception e) {e.printStackTrace()}
	}

	def sync(int nextHop, ArrayList table, ArrayList modules) {
		def origin = arrayByHop(nextHop)
		try {
		// println wrapper

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
			def localTable = local?.get(row[this.destination])
			if(!localTable || (localTable[this.metric] != 16 && row[this.metric] < localTable[this.metric]))
				add(row[this.destination], row[this.metric] + 1, nextHop)
		}

		missing.each { redirect ->
			remove(redirect[this.destination])
		}

		} catch (Exception e) { e.printStackTrace() }

	}

	def arrayByHop(int dst) {
		new ArrayList(wrapper?.values())
				.stream()
				.filter(x ->
					dst == x[this.hop]
				)
				.collect()
	}

	def getFlushListMetric() {
		new ArrayList(wrapper?.values())
			.stream()
			.filter(x ->
				x[this.metric] == 16
			)
			.collect()
	}

	def flush(ArrayList flushListNotUpdate) {
		getFlushListMetric()?.each{ item ->
			remove(item[this.destination])
		}

		flushListNotUpdate?.each { local ->
			def destination = getForwardingByLocalPort(local)
			if(destination)
				arrayByHop(destination)?.each{ it ->
					add(it[this.destination], 16, it[this.hop])
				}
		}
	}

	def remove(int destination) {
		try {
			def key = destinationKey.get(destination) 
			def hop = wrapper?.get(key)
			if(hop) {
			hop = hop[this.hop]
				removeForwarding(hop)
				ArrayList arr = hopKey.get(hop) as ArrayList
				arr?.remove(arr.indexOf(key))
			}

			destinationKey.remove(destination)
			wrapper.remove(key)
		} catch(Exception e) {e.printStackTrace()}
	}

	def getList(){
		new ArrayList(wrapper?.values())
	}

	def getReplyList() {
		new ArrayList(wrapper?.values())
				.stream()
		        .flatMap(entry ->
		        	Stream.of(
		        		entry, new int[] { entry[0], entry[1] }
	        		)
	        	)
				.filter(x -> x.size() < 3)
				.filter(x -> x[this.metric] < 16)
		        .toArray()
	}

	def generateKey() {
		int key = 0
		
		do {
			key = new Random().nextInt(11711)
		} while (wrapper.containsKey(key))

		key
	}

	// ---------------------------------------------------------------

	def forwarding(int nextHop, int local) {
		forwarding.put(nextHop, local)
		reverse.put(local, nextHop)
	}

	def getForwaringPort(int nextHop) {
		forwarding.get(nextHop)
	}

	def getForwardingByLocalPort(int local) {
		reverse.get(local)
	}

	def removeForwarding(int local) {
		forwarding.remove(reverse.get(local))
		forwarding.remove(local)
	}


	// ---------------------------------------------------------------

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
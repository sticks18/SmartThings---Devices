/**
 *  PetSafe Smart Door
 *
 *  Copyright 2015 Scott Gibson
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *
 *
 */
metadata {
	// Automatically generated. Make future change here.
	definition (name: "PetSafe Smart Door", namespace: "sticks18", author: "Scott Gibson") {
		capability "Battery"
		capability "Polling"
		capability "Actuator"
		capability "Refresh"
		capability "Lock"

		fingerprint profileId: "0104", inClusters: "0000 0001 0003 0009 000A 0101", outClusters: "0000 0001 0003 0009 000A 0101"
	}


		simulator {
		/*  // status "locked": "command: 9881, payload: 00 62 03 FF 00 00 FE FE"
		// status "unlocked": "command: 9881, payload: 00 62 03 00 00 00 FE FE"

		// reply "9881006201FF,delay 4200,9881006202": "command: 9881, payload: 00 62 03 FF 00 00 FE FE"
		// reply "988100620100,delay 4200,9881006202": "command: 9881, payload: 00 62 03 00 00 00 FE FE"  */
	}

	tiles {
		standardTile("toggle", "device.lock", width: 2, height: 2) {
			state "locked", label:'locked', action:"lock.unlock", icon:"st.locks.lock.locked", backgroundColor:"#79b821", nextState:"unlocking"
			state "unlocked", label:'unlocked', action:"lock.lock", icon:"st.locks.lock.unlocked", backgroundColor:"#ffffff", nextState:"locking"
			state "unknown", label:"unknown", action:"lock.lock", icon:"st.locks.lock.unknown", backgroundColor:"#ffffff", nextState:"locking"
			state "locking", label:'locking', icon:"st.locks.lock.locked", backgroundColor:"#79b821"
			state "unlocking", label:'unlocking', icon:"st.locks.lock.unlocked", backgroundColor:"#ffffff"
		}
		standardTile("lock", "device.lock", inactiveLabel: false, decoration: "flat") {
			state "default", label:'lock', action:"lock.lock", icon:"st.locks.lock.locked", nextState:"locking"
		}
		standardTile("unlock", "device.lock", inactiveLabel: false, decoration: "flat") {
			state "default", label:'unlock', action:"lock.unlock", icon:"st.locks.lock.unlocked", nextState:"unlocking"
		}
		valueTile("battery", "device.battery", inactiveLabel: false, decoration: "flat") {
			state "battery", label:'${currentValue}% battery', unit:""
		}
		standardTile("refresh", "device.lock", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}

		main "toggle"
		details(["toggle", "lock", "unlock", "battery", "refresh"])
	}
}

def parse(String description) {
	
    log.trace description
    
    def msg = zigbee.parse(description)
    log.trace msg
    
    def results = []
	if (description?.startsWith('catchall:')) {
		results = parseCatchAllMessage(description)
	}
	else if (description?.startsWith('read attr -')) {
		results = parseReportAttributeMessage(description)
	}

}

private parseReportAttributeMessage(String description) {
	
    Map descMap = (description - "read attr - ").split(",").inject([:]) { map, param ->
	def nameAndValue = param.split(":")
	map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
	}

	//log.debug "Desc Map: $descMap"
	def results = []
	if (descMap.cluster == "0001" && descMap.attrId == "0020") {
		log.debug "Received battery level report"
		results = createEvent(getBatteryResult(Integer.parseInt(descMap.value, 16)))
	}
    else if (descMap.cluster == "0101" && descMap.attrId == "0000") {
		log.debug "Received lock status"
		def linkText = getLinkText(device)
        
        if(descMap.value == "01"){
        	results = createEvent( name: 'lock' , value: "locked", descriptionText: "${linkText} is locked")
		}
        else if(descMap.value == "02") {
        	results = createEvent( name: 'lock' , value: "unlocked", descriptionText: "${linkText} is unlocked")
        }
            
    }
	
    return results
    
}

private getBatteryResult(rawValue) {

	//log.debug 'Battery'
	def linkText = getLinkText(device)
	def result = [ name: 'battery' ]

	def volts = rawValue / 10
	def descriptionText
	if (volts > 6.5) {
		result.descriptionText = "${linkText} battery has too much power (${volts} volts)."
	}
	else {
		def minVolts = 4.0
		def maxVolts = 6.0
		def pct = (volts - minVolts) / (maxVolts - minVolts)
		result.value = Math.min(100, (int) pct * 100)
		result.descriptionText = "${linkText} battery was ${result.value}%"
	}

	return result
    
}

private Map parseCatchAllMessage(String description) {

	def results = [:]
	def cluster = zigbee.parse(description)
	
    if (shouldProcessMessage(cluster)) {
		switch(cluster.clusterId) {
			case 0x0001:
				results << createEvent(getBatteryResult(cluster.data.last()))
				break
		}
	}
	
    return results
    
}

private boolean shouldProcessMessage(cluster) {
	// 0x0B is default response indicating message got through
	// 0x07 is bind message
	
    boolean ignoredMessage = cluster.profileId != 0x0104 ||
		cluster.command == 0x0B ||
		cluster.command == 0x07 ||
		(cluster.data.size() > 0 && cluster.data.first() == 0x3e)
	
    return !ignoredMessage
    
}

def poll() {
	
    log.debug "Executing 'poll'"
	refresh()
}

def refresh() {
	
    [
	"st rattr 0x${device.deviceNetworkId} 0x0C 0x101 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 0x0C 1 0x20"
    ]
    
}

def updated() {

	configure()
    
}

def lock() {
	
    log.debug "Executing 'lock'"
    sendEvent(name: "lock", value: "locked")
	"st cmd 0x${device.deviceNetworkId} 0x0C 0x101 0 {}"
    
}

def unlock() {
	
    log.debug "Executing 'unlock'"
	sendEvent(name: "lock", value: "unlocked")
	"st cmd 0x${device.deviceNetworkId} 0x0C 0x101 1 {}"
    
}

def configure() {

	String zigbeeId = swapEndianHex(device.hub.zigbeeId)
	log.debug "Confuguring Reporting and Bindings."
	def configCmds = [	

        //Lock Reporting
        "zcl global send-me-a-report 0x101 0 0x30 0 3600 {01}", "delay 500",
        "send 0x${device.deviceNetworkId} 1 1", "delay 1000",

        //Battery Reporting
        "zcl global send-me-a-report 1 0x20 0x20 5 3600 {}", "delay 200",
        "send 0x${device.deviceNetworkId} 1 1", "delay 1500",

        "zdo bind 0x${device.deviceNetworkId} 0x0C 1 0x101 {${device.zigbeeId}} {}", "delay 500",
        "zdo bind 0x${device.deviceNetworkId} 0x0C 1 1 {${device.zigbeeId}} {}"
    
    ]
    return configCmds + refresh() // send refresh cmds as part of config
}

private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}

private String swapEndianHex(String hex) {
    reverseArray(hex.decodeHex()).encodeHex()
}

private byte[] reverseArray(byte[] array) {
    int i = 0;
    int j = array.length - 1;
    byte tmp;
    while (j > i) {
        tmp = array[j];
        array[j] = array[i];
        array[i] = tmp;
        j--;
        i++;
    }
    return array
}

private getEndpointId() {
	new BigInteger(device.endpointId, 16).toString()
}

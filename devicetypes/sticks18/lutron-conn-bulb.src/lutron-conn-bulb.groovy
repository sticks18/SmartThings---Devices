/**
 *  
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
 */
/* Lutron Connected Bulb Remote

Capabilities:
  Actuator
  Configuration
  Polling
  Refresh
  Switch
  Switch Level
  
    
*/

metadata {
	definition (name: "Lutron Connected Bulb Remote", namespace: "Sticks18", author: "Scott G") {
		capability "Actuator"
		capability "Button"
		capability "Configuration"
		capability "Sensor"
        capability "Refresh"
        
        command "getGroup"
        command "assignGroup"
        command "removeGroup"
        command "removeAllGroup"
        command "updateGroup"
        
        attribute "groupId", "String"
        
		fingerprint profileId: "C05E", deviceId: "0820", inClusters: "0000,1000,FF00,FC44", outClusters: "0000,0003,0004,0005,0006,0008,1000,FF00"
	}

	// simulator metadata
	simulator {
		// status messages

	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.button", width: 2, height: 2) {
			state "default", label: "", icon: "st.unknown.zwave.remote-controller", backgroundColor: "#ffffff"
		}
        standardTile("refresh", "device.refresh", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        valueTile("group", "device.groupId", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
            state "groupId", label: '${currentValue}'
        }
		main "button"
		details(["button", "group"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
    log.trace description
}

def getGroup(zigId, endP) {

	"st cmd 0x${zigId} ${endP} 4 2 {}"

}

def assignGroup(zigId, endP, grpId) {

	"st cmd 0x${zigId} ${endP} 4 0 {${grpId}}"

}

def removeGroup(zigId, endP, grpId) {

	"st cmd 0x${zigId} ${endP} 4 3 {${grpId}}"

}

def removeAllGroup(zigId, endP) {

	"st cmd 0x${zigId} ${endP} 4 4 {}"

}

def updateGroup(grpId) {
	log.debug "Group Id for this remote is: ${grpId}"
    sendEvent(name: "groupId", value: grpId)
}

def refresh() {
	[
	/* "raw 0x1000 {00 01 0C 0000 FE}", "delay 150",
     "send 0x${device.deviceNetworkId} 1 1"
    "st cmd 0x${device.deviceNetworkId} 1 0x1000 0x41 {01}", "delay 500",
    "st cmd 0x${device.deviceNetworkId} 1 0x1000 0x42 {01}", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 0 1", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 0 2", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 0 3", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 1 0 4", "delay 500", 
    "st rattr 0x${device.deviceNetworkId} 1 0 5" */
    ]

}

def configure() {

	log.debug "configure"
	String zigbeeId = swapEndianHex(device.hub.zigbeeId)
	log.debug "Confuguring Reporting and Bindings."
	def configCmds = [	
    
        "zdo bind 0x${device.deviceNetworkId} 1 1 6 {${device.zigbeeId}} {}", "delay 1000",
		"zdo bind 0x${device.deviceNetworkId} 1 1 8 {${device.zigbeeId}} {}", "delay 500",
        
	]
    return configCmds + refresh
}


private getEndpointId() {
	new BigInteger(device.endpointId, 16).toString()
}

//Need to reverse array of size 2
private byte[] reverseArray(byte[] array) {
    byte tmp;
    tmp = array[1];
    array[1] = array[0];
    array[0] = tmp;
    return array
}

private String swapEndianHex(String hex) {
    reverseArray(hex.decodeHex()).encodeHex()
}


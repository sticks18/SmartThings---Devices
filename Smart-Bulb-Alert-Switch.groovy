/**
 *  Copyright 2015 SmartThings
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
 *  Smart Bulb Alert Switch - to be used with Smart Bulb Alert SmartApp to trigger smart bulbs to blink/flash
 *
 *  Author: Scott Gibson
 *
 *  Date: 2015-25-11
 */
metadata {
	definition (name: "Smart Bulb Alert Switch", namespace: "sticks18", author: "Scott Gibson") {
		capability "Actuator"
		capability "Switch"
		
		command "zigbeeCmd"
		command "attWrite"
		command "allOff"
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("allOff", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:"Stop Alerts", action:"device.allOff", icon:"st.secondary.refresh"
		}
		main "button"
		details "button", "allOff"
	}
}

def parse(String description) {
}

def on() {
	sendEvent(name: "switch", value: "on")
	parent.on(this)
}

def off() {
	sendEvent(name: "switch", value: "off")
	parent.off(this)
}

def zigbeeCmd(netId, endpoint, cluster, command, payload) {
  log.debug "Sending zigbee command ${cluster} ${command} {${payload}} to device ${netId} via endpoint ${endpoint}"
  "st cmd 0x${netId} ${endpoint} ${cluster} ${command} {${payload}}"
}

def attWrite(netId, endpoint, cluster, attribute, size, payload) {
  log.debug "Writing zigbee attribute ${cluster} ${command} {${payload}} to device ${netId} via endpoint ${endpoint}"
  "st wattr 0x${netId} ${endpoint} ${cluster} ${attribute} ${size} {${payload}}"
}

def allOff(){
  log.debug "Stop all bulbs alerting"
  sendEvent(name: "switch", value: "off")
	parent.allOff(this)
}

private hex(value, width) {
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

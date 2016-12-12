/**
* Copyright 2015 SmartThings
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at:
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
* on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
* for the specific language governing permissions and limitations under the License.
*
* CentraLite Thermostat
*
* Author: SmartThings
* Date: 2013-12-02
*/
metadata {
definition (name: "Heater", namespace: "smartthings", author: "SmartThings") {
capability "Actuator"
capability "Temperature Measurement"
capability "Thermostat"
capability "Configuration"
capability "Power Meter"
capability "Refresh"
capability "Sensor"
capability "Switch"
capability "Polling"
	
command "ecoOn"
command "ecoOff"

attribute "ecoMode", "string"
	
fingerprint profileId: "0104", inClusters: "0300,0000,0003,0006,0201,0204,0702,0B05", outClusters: "0003,0019,0020"

}

// simulator metadata
simulator { 
// status messages
	status "on": "on/off: 1"
	status "off": "on/off: 0"
// reply messages
reply "zcl on-off on": "on/off: 1"
reply "zcl on-off off": "on/off: 0"

}

tiles {
	valueTile("temperature", "device.temperature", width: 2, height: 2) {
	state("temperature", label:'${currentValue}°', unit:"F",
	backgroundColors:[
	[value: 31, color: "#153591"],
	[value: 44, color: "#1e9cbb"],
	[value: 59, color: "#90d2a7"],
	[value: 74, color: "#44b621"],
	[value: 84, color: "#f1d801"],
	[value: 95, color: "#d04e00"],
	[value: 96, color: "#bc2323"]
	]
	)
	}
	standardTile("switch", "device.switch", inactiveLabel: false, decoration: "flat") {
		state "off", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff"
 		state "on", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#79b821"
 		state "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
 		state "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
	}
	controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 1, width: 3, inactiveLabel: false, range: "(50..97)") {
		state "setHeatingSetpoint", action:"thermostat.setHeatingSetpoint", backgroundColor:"#d04e00"
	}
	valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
	state "heat", label:'${currentValue}° heat', unit:"F", backgroundColor:"#ffffff"
	}
	standardTile("refresh", "device.temperature", inactiveLabel: false, decoration: "flat") {
	state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
	}
	standardTile("ecoMode", "device.ecoMode", inactiveLabel: false, decoration: "flat") {
		state "Eco off", label:'${currentValue}', action:"ecoOn", icon:"st.Outdoor.outdoor19", backgroundColor:"#ffffff"
 		state "Eco on", label:'${currentValue}', action:"ecoOff", icon:"st.Outdoor.outdoor19", backgroundColor:"#79b821"
 		state "turningOn", label:'${currentValue}', action:"ecoOff", icon:"st.Outdoor.outdoor19", backgroundColor:"#79b821", nextState:"turningOff"
		state "turningOff", label:'${currentValue}', action:"ecoOn", icon:"st.Outdoor.outdoor19", backgroundColor:"#ffffff", nextState:"turningOn"
	}
main "temperature"
details(["temperature", "switch", "heatSliderControl", "heatingSetpoint", "ecoMode", "refresh"])

}
}

// parse events into attributes

def parse(String description) {
  log.debug "Parse description $description"
  def map = [:]
  if (description?.startsWith("read attr -")) {
    	def descMap = parseDescriptionAsMap(description)
    	log.debug "Desc Map: $descMap"
    	if (descMap.cluster == "0201" && descMap.attrId == "0000") {
      		log.debug "TEMP"
      		map.name = "temperature"
      		map.value = getTemperature(descMap.value)
      		map.unit = temperatureScale
    	} else if (descMap.cluster == "0201" && descMap.attrId == "0012") {
      		log.debug "HEATING SETPOINT"
      		map.name = "heatingSetpoint"
      		map.value = getTemperature(descMap.value)
      		map.unit = temperatureScale
    	} else if (descMap.cluster == "0201" && descMap.attrId == "0025") {
      		log.debug "EcoMode"
      		map.name = "ecoMode"
      		map.value = (descMap.value == "04" ? "Eco on" : "Eco off")
    	} else if (descMap.cluster == "0201" && descMap.attrId == "001c") {
      		log.debug "MODE"
      		map.name = "switch"
      		map.value = (descMap.value == "00" ? "off" : "on")
	} else if (descMap.cluster == "0702") {
		log.debug "Power"
		map.name = "power"
		map.value = convertHexToInt(descMap.value)
	} 
    } else if (description?.startsWith("catchall:")) {
    	def msg = zigbee.parse(description)
    	log.trace msg
    	log.trace "data: $msg.data"
    }

  def result = null
  if (map) {
	  result = createEvent(map)
  }
  log.debug "Parse returned $map"
  return result
}

def parseDescriptionAsMap(description) {
  (description - "read attr - ").split(",").inject([:]) { map, param ->
  def nameAndValue = param.split(":")
  map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
  }
}

def poll() {
	refresh()
}

def refresh() {
	log.debug "refresh called"
	[
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x12", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x1C", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x25", "delay 200",
		zigbee.simpleMeteringPowerRefresh()
	]
}

def ecoOn() {
	log.debug "ecoMode on"
	sendEvent(name: "ecoMode", value: "Eco on")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x25 0x18 {04}"
}

def ecoOff() {
	log.debug "ecoMode off"
	sendEvent(name: "ecoMode", value: "Eco off")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x25 0x18 {00}"
}

def getTemperature(value) {
	if (value != null) {
		def celsius = Integer.parseInt(value, 16) / 100
		if (getTemperatureScale() == "C") {
			return celsius
		} else {
			return Math.round(celsiusToFahrenheit(celsius))
		}
	}
}

def setHeatingSetpoint(degrees) {
	if (degrees != null) {
		def temperatureScale = getTemperatureScale()
		def degreesInteger = Math.round(degrees)
		log.debug "setHeatingSetpoint({$degreesInteger} ${temperatureScale})"
		sendEvent("name": "heatingSetpoint", "value": degreesInteger, "unit": temperatureScale)

		def celsius = (getTemperatureScale() == "C") ? degreesInteger : (fahrenheitToCelsius(degreesInteger) as Double).round(2)
		"st wattr 0x${device.deviceNetworkId} 1 0x201 0x12 0x29 {" + hex(celsius * 100) + "}"

	}
}

def heat() {
	log.debug "heat"
	sendEvent("name":"thermostatMode", "value":"heat")
	sendEvent("name":"switch", "value":"on")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {04}"
}

def on() {
	log.debug "on"
	sendEvent("name":"thermostatMode", "value":"auto")
	sendEvent("name":"switchStatus", "value":"on")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {01}"
}

def off() {
	log.debug "off"
	sendEvent("name":"thermostatMode", "value":"off")
	sendEvent("name":"switch", "value":"off")
	sendEvent("name":"timer", "value":"off")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {00}"
}

private hex(value) {
	new BigInteger(Math.round(value).toString()).toString(16)
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

def configure() {

	log.debug "binding to Thermostat cluster"
	[
		"zdo bind 0x${device.deviceNetworkId} 1 1 0x201 {${device.zigbeeId}} {}", "delay 500",
		"zcl global send-me-a-report 0x201 0x0000 0x29 20 300 {19 00}",  // report temperature changes over 0.2C 
			"send 0x${device.deviceNetworkId} 1 1", "delay 500", 
		"zcl global send-me-a-report 0x201 0x001C 0x30 10 305 { }",  // mode 
			"send 0x${device.deviceNetworkId} 1 1","delay 500",
		"zcl global send-me-a-report 0x201 0x0025 0x18 10 310 { 00 }",  // schedule on/off
			"send 0x${device.deviceNetworkId} 1 1","delay 500",
		"zcl global send-me-a-report 0x201 0x0012 0x29 10 320 {32 00}", // cooling setpoint delta: 0.5C (0x3200 in little endian)
			"send 0x${device.deviceNetworkId} 1 1","delay 500",
		zigbee.simpleMeteringPowerConfig()
	]
}

def updated() {
	configure()
}

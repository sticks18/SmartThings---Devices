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
	
command "switchOn"
command "switchOff"

attribute "switchStatus", "string"
	
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
	controlTile("heatSliderControl", "device.heatingSetpoint", "slider", height: 1, width: 2, inactiveLabel: false, range: "(50..97)") {
	state "setHeatingSetpoint", action:"thermostat.setHeatingSetpoint", backgroundColor:"#d04e00"
	}
	valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
	state "heat", label:'${currentValue}° heat', unit:"F", backgroundColor:"#ffffff"
	}
	standardTile("refresh", "device.temperature", inactiveLabel: false, decoration: "flat") {
	state "default", action:"refresh.refresh", icon:"st.secondary.refresh"
	}
	standardTile("switchClust", "device.switchStatus", inactiveLabel: false, decoration: "flat") {
		state "off", label:'${currentValue}', action:"switchOn", icon:"st.switches.switch.off", backgroundColor:"#ffffff"
 		state "on", label:'${currentValue}', action:"switchOff", icon:"st.switches.switch.on", backgroundColor:"#79b821"
 		state "turningOn", label:'${currentValue}', action:"switchOff", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
		state "turningOff", label:'${currentValue}', action:"switchOn", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
	}
	valueTile ("power", "device.power", inactiveLabel: false, decoration: "flat") {
                state "power", label:'${currentValue} W', backgroundColor: "#ffffff"
        }
main "temperature"
details(["temperature", "switchClust", "heatSliderControl", "heatingSetpoint", "refresh", "switch", "power"])

}
}

// parse events into attributes

def parse(String description) {
  log.debug "Parse description $description"
  def map = [:]
  def event = zigbee.getEvent(description)
    if (event) {
        log.info event
        if (event.name == "power") {
            if (device.getDataValue("manufacturer") != "OSRAM") {       //OSRAM devices do not reliably update power
                event.value = (event.value as Integer) / 10             //TODO: The divisor value needs to be set as part of configuration
                sendEvent(event)
            }
        }
        else {
            sendEvent(event)
        }
    } else if (description?.startsWith("read attr -")) {
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
    	} else if (descMap.cluster == "0201" && descMap.attrId == "001c") {
      		log.debug "MODE"
      		map.name = "switchStatus"
      		map.value = (descMap.value == "00" ? "off" : "on")
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

def refresh() {
	log.debug "refresh called"
	[
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x12", "delay 200",
		"st rattr 0x${device.deviceNetworkId} 1 0x201 0x1C", "delay 200",
		zigbee.onOffRefresh() + zigbee.simpleMeteringPowerRefresh()
	]
}

def on() {
	// just assume it works for now
	log.debug "on()"
	sendEvent(name: "switch", value: "on")
	"st cmd 0x${device.deviceNetworkId} 1 6 1 {}"
}

def off() {
	// just assume it works for now
	log.debug "off()"
	sendEvent(name: "switch", value: "off")
	"st cmd 0x${device.deviceNetworkId} 1 6 0 {}"
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

def switchOn() {
	log.debug "on"
	sendEvent("name":"thermostatMode", "value":"auto")
	sendEvent("name":"switchStatus", "value":"on")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {01}"
}

def switchOff() {
	log.debug "off"
	sendEvent("name":"thermostatMode", "value":"off")
	sendEvent("name":"switchStatus", "value":"off")
	"st wattr 0x${device.deviceNetworkId} 1 0x201 0x1C 0x30 {00}"
}

private hex(value) {
	new BigInteger(Math.round(value).toString()).toString(16)
}

def configure() {

	log.debug "binding to Thermostat cluster"
	[
		"zdo bind 0x${device.deviceNetworkId} 1 1 0x201 {${device.zigbeeId}} {}", "delay 200",
		zigbee.onOffConfig() + zigbee.simpleMeteringPowerConfig()
	]
}

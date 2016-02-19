/**
 *  GE Link Bulb
 *
 *  Copyright 2014 SmartThings
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
 *  Thanks to Chad Monroe @cmonroe and Patrick Stuart @pstuart, and others
 *
 ******************************************************************************
 *                                Changes
 ******************************************************************************
 *
 *  Change 1:	2014-10-10 (wackford)
 *				Added setLevel event so subscriptions to the event will work
 *  Change 2:	2014-12-10 (jscgs350 using Sticks18's code and effort!)
 *				Modified parse section to properly identify bulb status in the app when manually turned on by a physical switch
 *  Change 3:	2014-12-12 (jscgs350, Sticks18's)
 *				Modified to ensure dimming was smoother, and added fix for dimming below 7
 *	Change 4:	2014-12-14 Part 1 (Sticks18)
 *				Modified to ignore unnecessary level change responses to prevent level skips
 *	Change 5:	2014-12-14 Part 2 (Sticks18, jscgs350)
 *				Modified to clean up trace&debug logging, added new code from @sticks18 for parsing "on/off" to determine if the bulb is manually turned on and immediately update the app
 *	Change 6:	2015-01-02	(Sticks18)
 *				Modified to allow dim rate in Preferences. Added ability to dim during On/Off commands and included this option in Preferences. Defaults are "Normal" and no dim for On/Off.
 *	Change 7:	2015-01-09	(tslagle13)
 *				dimOnOff is was boolean, and switched to enum. Properly update "rampOn" and "rampOff" when refreshed or a polled (dim transition for On/Off commands)
 *	Change 8:	2015-03-06	(Juan Risso)
 *				Slider range from 0..100
 *	Change 9:	2015-03-06	(Juan Risso)
 *				Setlevel -> value to integer (to prevent smartapp calling this function from not working).
 *  Change 10:  2015-09-06  (Sticks18)
 *              Modified tile layout to make use of new multiattribute tile for dimming. Added dim adjustment when sending setLevel() if bulb is off, so it will transition smoothly from 0.
 *  Change 11:  2016-01-20 (Sticks18)
 *              Completely overhauled the code for the new zigbee implementation layers.
 *
 *
 */
metadata {
	definition (name: "GE Link Pro", namespace: "sticks18", author: "Scott G") {

    	capability "Actuator"
        capability "Configuration"
        capability "Refresh"
		capability "Sensor"
        capability "Switch"
		capability "Switch Level"
        capability "Polling"
        
        command "alert"
        command "toggle"
        
        attribute "attDimRate", "string"
        attribute "attDimOnOff", "string"
        attribute "alert", "string"

		fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,1000", outClusters: "0019"
	}

	// UI tile definitions
	tiles(scale: 2) {
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true, canChangeBackground: true) {
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
                  attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.light.off", backgroundColor: "#ffffff", nextState: "turningOn"
			      attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.light.on", backgroundColor: "#79b821", nextState: "turningOff"
                  attributeState "turningOff", label: '${name}', action: "switch.on", icon: "st.switches.light.off", backgroundColor: "#ffffff", nextState: "turningOn"
			      attributeState "turningOn", label: '${name}', action: "switch.off", icon: "st.switches.light.on", backgroundColor: "#79b821", nextState: "turningOff"
            }
            tileAttribute("device.level", key: "SLIDER_CONTROL") {
                  attributeState "level", action:"switch level.setLevel"
            }
            tileAttribute("level", key: "SECONDARY_CONTROL") {
                  attributeState "level", label: 'Light dimmed to ${currentValue}%'
            }    
		}
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        valueTile("attDimRate", "device.attDimRate", inactiveLabel: false, decoration: "flat", width: 4, height: 1) {
			state "attDimRate", label: 'Dim rate: ${currentValue}'
		}
        valueTile("attDimOnOff", "device.attDimOnOff", inactiveLabel: false, decoration: "flat", width: 4, height: 1) {
			state "attDimOnOff", label: 'Dim for on/off: ${currentValue}'
		}
		controlTile("levelSliderControl", "device.level", "slider", height: 2, width: 6, inactiveLabel: false, range: "(0..100)") {
			state "level", action:"switch level.setLevel"
		}
        
        
		main "switch"
		details(["switch","attDimRate", "refresh", "attDimOnOff","levelSliderControl"])
	}
	
	    preferences {
        
        	input("dimRate", "enum", title: "Dim Rate", options: ["Instant", "Normal", "Slow", "Very Slow"], defaultValue: "Normal", required: false, displayDuringSetup: true)
            input("dimOnOff", "enum", title: "Dim transition for On/Off commands?", options: ["Yes", "No"], defaultValue: "No", required: false, displayDuringSetup: true)
            
    }
}

// Parse incoming device messages to generate events
def parse(String description) {
	log.debug "description is $description"

    def resultMap = zigbee.getKnownDescription(description)
    if (resultMap) {
        log.info resultMap
        if (resultMap.type == "update") {
            log.info "$device updates: ${resultMap.value}"
        }
        else {
            sendEvent(name: resultMap.type, value: resultMap.value)
        }
    }
    else {
        log.warn "DID NOT PARSE MESSAGE for description : $description"
        log.debug zigbee.parseDescriptionAsMap(description)
    }

}

def alert(alertTime = 7) {
    log.debug alertTime
    def payload = swapEndianHex(zigbee.convertToHexString(alertTime,4))
    zigbee.command(0x0003, 0x00, payload)
}

def toggle() {
	zigbee.command(0x0006, 0x02)
}

def poll() { refresh() }

def updated() {
	
    sendEvent( name: "attDimRate", value: "${dimRate}" )
    sendEvent( name: "attDimOnOff", value: "${dimOnOff}" )
    
	state.dOnOff = "0000"
    
	if (dimRate) {

		switch (dimRate) 
        	{

        		case "Instant":

            		state.rate = "0000"
                	if (dimOnOff) { state.dOnOff = "0000"}
                    break

            	case "Normal":

            		state.rate = "1500"
                    if (dimOnOff) { state.dOnOff = "0015"}
                	break

            	case "Slow":

            		state.rate = "2500"
                    if (dimOnOff) { state.dOnOff = "0025"}
               		break
                
            	case "Very Slow":
            
            		state.rate = "3500"
                    if (dimOnOff) { state.dOnOff = "0035"}
                	break

        	}
    
    }
    
    else {
    
    	state.rate = "1500"
        state.dOnOff = "0000"
        
    }
    
        if (dimOnOff == "Yes"){
			switch (dimOnOff){
        		case "InstantOnOff":

            		state.rate = "0000"
                	if (state.rate == "0000") { state.dOnOff = "0000"}
                    break

            	case "NormalOnOff":

            		state.rate = "1500"
                    if (state.rate == "1500") { state.dOnOff = "0015"}
                	break

            	case "SlowOnOff":

            		state.rate = "2500"
                    if (state.rate == "2500") { state.dOnOff = "0025"}
               		break
                
            	case "Very SlowOnOff":
            
            		state.rate = "3500"
                    if (state.rate == "3500") { state.dOnOff = "0035"}
                	break

        	}
            
    }
    else{
    	state.dOnOff = "0000"
    }
    
    zigbee.writeAttribute(0x0008, 0x10, 0x21, state.dOnOff)

}

def on(onTime = null) {
    if (onTime) {
    	def newTime = onTime * 10
		def finalHex = swapEndianHex(zigbee.convertToHexString(newTime, 4))
        zigbee.command(0x0006, 0x42, "00", finalHex, "0000")
    }
    else {
    	zigbee.on()
    }
	
}

def off() {
    zigbee.off()
}

def refresh() {
    
    zigbee.onOffRefresh() + zigbee.levelRefresh() + zigbee.writeAttribute(0x0008, 0x10, 0x21, state.dOnOff) + zigbee.onOffConfig() + zigbee.levelConfig()
    
}

def setLevel(value, duration = 2.0) {

    value = (value * 255 / 100)
    def level = zigbee.convertToHexString(value,2);
    
    duration = duration * 10
    def tranTime = swapEndianHex(zigbee.convertToHexString(duration, 4))
	
    zigbee.command(0x0008, 0x04, level, tranTime)
    
}

def configure() {
	log.debug "Configuring Reporting and Bindings."
    zigbee.onOffConfig() + zigbee.levelConfig() + zigbee.onOffRefresh() + zigbee.levelRefresh()
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

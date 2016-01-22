/**
 *  Dresden RGBW Lightstrip controller
 *
 *   Removed the color adjuster.  Added parsers for level and hue
 *  
 *
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
 */

metadata {
	// Automatically generated. Make future change here.
	definition (name: "Dresden FLS-PP", namespace: "Sticks18", author: "Scott G") {
		capability "Switch Level"
		capability "Actuator"
		capability "Color Control"
		capability "Switch"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
       
        command "setWhiteLevel"
        command "whiteOn"
        command "whiteOff"
       
        command "setAdjustedColor"
        command "startLoop"
        command "stopLoop"
        command "setLoopTime"
        command "setDirection"
        
        command "alert"
        command "toggle"
        
        // This is a new temporary counter to keep track of no responses
        attribute "unreachable", "number"
        attribute "colorMode", "string"
        attribute "colorName", "string"
        attribute "switchColor", "string"
        attribute "loopActive", "string"
        attribute "loopDirection", "string"
        attribute "loopTime", "number"
        attribute "alert", "string"
        attribute "whiteLevel", "number"
        attribute "whiteSwitch", "string"

		fingerprint profileId: "C05E", inClusters: "0000,0003,0004,0005,0006,0008,0300,1000", outClusters: "0019"
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

	// UI tile definitions
	tiles (scale: 2){
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        	tileAttribute("device.switchColor", key: "PRIMARY_CONTROL") {
				attributeState "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
            	attributeState "Red", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff0000"
            	attributeState "Brick Red", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff3700"
    			attributeState "Safety Orange", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff6F00"
    			attributeState "Dark Orange", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff9900"
            	attributeState "Amber", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ffbf00"
            	attributeState "Gold", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ffe1000"
            	attributeState "Yellow", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ffff00"
    			attributeState "Electric Lime", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#bfff00"
            	attributeState "Lawn Green", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#88ff00"
            	attributeState "Bright Green", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#48ff00"
            	attributeState "Lime", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ff11"
            	attributeState "Spring Green", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ff6a"
            	attributeState "Turquoise", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ffd0"
            	attributeState "Aqua", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00ffff"
            	attributeState "Sky Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00bfff"
            	attributeState "Dodger Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#007bff"
            	attributeState "Navy Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#0050ff"
            	attributeState "Blue", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#0000ff"
            	attributeState "Han Purple", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#3b00ff"
            	attributeState "Electric Indigo", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#6600ff"
            	attributeState "Electric Purple", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#b200ff"
            	attributeState "Orchid Purple", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#e900ff"
            	attributeState "Magenta", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff00dc"
            	attributeState "Hot Pink", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff00aa"
            	attributeState "Deep Pink", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff007b"
            	attributeState "Raspberry", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff0061"
            	attributeState "Crimson", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#ff003b"
            	attributeState "White", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
                attributeState "Color Loop", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			}
            tileAttribute ("device.color", key: "COLOR_CONTROL") {
            	attributeState "color", action: "setAdjustedColor"
            }
            tileAttribute ("device.level", key: "SECONDARY_CONTROL") {
            	attributeState "level", label: 'Level is ${currentValue}%'
            }
        }
        standardTile("refresh", "device.switch", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		controlTile("levelSliderControl", "device.level", "slider", height: 2, width: 4, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
        controlTile("whiteLevelSliderControl", "device.whiteLevel", "slider", height: 2, width: 4, inactiveLabel: false, range: "(0..100)") {
            state "whiteLevel", action:"setWhiteLevel"
        }
        standardTile("whiteChannel", "device.whiteSwitch", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
            state "off", label: "White Off", action: "whiteOn", backgroundColor: "#ffffff"
            state "on", label: "White On", action: "whiteOff", backgroundColor: "#79b821"
        }
        valueTile("colorName", "device.colorName", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
            state "colorName", label: '${currentValue}'
        }
        standardTile("loop", "device.loopActive", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "Active", label:'${currentValue}', action: "stopLoop", backgroundColor: "#79b821", nextState: "stoppingLoop"
            state "startingLoop", label: "Starting Loop", action: "stopLoop", backgroundColor: "#79b821", nextState: "stoppingLoop"
			state "Inactive", label:'${currentValue}', action: "startLoop", backgroundColor: "#ffffff", nextState: "startingLoop"
            state "stoppingLoop", label: "Stopping Loop", action: "startLoop", backgroundColor: "#ffffff", nextState: "startingLoop"
		}
        controlTile("loopTimeControl", "device.loopTime", "slider", height: 2, width: 6, range: "(1..60)", inactiveLabel: false) {
        	state "loopTime", action: "setLoopTime"
        }
        standardTile("loopDir", "device.loopDirection", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
        	state "default", label: '${currentValue}', action: "setDirection"
        }
        
        
		main(["switch"])
		details(["switch", "levelSliderControl", "refresh", "whiteLevelSliderControl", "whiteChannel", "colorName", "loop", "loopDir", "loopTimeControl"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
   log.info "description is $description"
    
    sendEvent(name: "unreachable", value: 0)
    if (device.currentValue("loopActive") == "Active") { 
    }
    else {
    if (description?.startsWith("catchall:")) {
        if(description?.endsWith("0100") ||description?.endsWith("1001") || description?.matches("on/off\\s*:\\s*1"))
        {
            def result = createEvent(name: "switch", value: "on")
            sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)
            log.debug "Parse returned ${result?.descriptionText}"
            return result
        }
        else if(description?.endsWith("0000") || description?.endsWith("1000") || description?.matches("on/off\\s*:\\s*0"))
        {
            if(!(description?.startsWith("catchall: 0104 0300") || description?.startsWith("catchall: 0104 0008"))){
                def result = createEvent(name: "switch", value: "off")
                sendEvent(name: "switchColor", value: "off", displayed: false)
                log.debug "Parse returned ${result?.descriptionText}"
                return result
            }
        }
    }
    else if (description?.startsWith("read attr -")) {
        def descMap = parseDescriptionAsMap(description)
       // log.trace "descMap : $descMap"

        if (descMap.cluster == "0300") {
            if(descMap.attrId == "0000"){  //Hue Attribute
                def hueValue = Math.round(convertHexToInt(descMap.value) / 255 * 100)
                log.debug "Hue value returned is $hueValue"
                def colorName = getColorName(hueValue)
    			sendEvent(name: "colorName", value: colorName)
                if (device.currentValue("switch") == "on") { sendEvent(name: "switchColor", value: ( device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false) }
                sendEvent(name: "hue", value: hueValue, displayed:false)
            }
            else if(descMap.attrId == "0001"){ //Saturation Attribute
                def saturationValue = Math.round(convertHexToInt(descMap.value) / 255 * 100)
                log.debug "Saturation from refresh is $saturationValue"
                sendEvent(name: "saturation", value: saturationValue, displayed:false)
            }
        }
        else if(descMap.cluster == "0008"){
            def dimmerValue = Math.round(convertHexToInt(descMap.value) * 100 / 255)
            log.debug "dimmer value is $dimmerValue"
            (descMap.sourceEndpoint == "0A" ? sendEvent(name: "level", value: dimmerValue) : sendEvent(name: "whiteLevel", value: dimmervalue))
        }
    }
    else {
        def name = description?.startsWith("on/off: ") ? "switch" : null
        if (name == "switch") {
            def value = (description?.endsWith(" 1") ? "on" : "off")
        	log.debug value
            sendEvent(name: "switchColor", value: (value == "off" ? "off" : device.currentValue("colorName")), displayed: false)
        }
        else { def value = null }
        def result = createEvent(name: name, value: value)
        log.debug "Parse returned ${result?.descriptionText}"
        return result
    }
	}
}

def parseDescriptionAsMap(description) {
    (description - "read attr - ").split(",").inject([:]) { map, param ->
        def nameAndValue = param.split(":")
        map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
    }
}

def on(onTime = null) {
	// just assume it works for now
	log.debug "on()"
	sendEvent(name: "switch", value: "on")
    sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)
    
    if (onTime) {
    	def newTime = onTime * 10
		def finalHex = swapEndianHex(hexF(newTime, 4))
        runIn(onTime, refresh)
        "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0x42 {00 ${finalHex} 0000}"
    }
    else {
    	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
    }
}

def off() {
	// just assume it works for now
	log.debug "off()"
	sendEvent(name: "loopActive", value: "Inactive")
    sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off", displayed: false)
	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
}

def toggle() {
	if (device.currentValue("switch") == "on") {
		sendEvent(name: "switch", value: "off")
		sendEvent(name: "switchColor", value: "off")
	}
	else {
		sendEvent(name: "switch", value: "on")
		sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)
	}
	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 2 {}"
}



def alert(action) {
	def value = "00"
	def valid = true
	switch(action) {
		case "Blink":
			value = "00"
			break
		case "Breathe":
			value = "01"
			break
		case "Okay":
			value = "02"
			break
		case "Stop":
			value = "ff"
			break
		default:
			valid = false
			break
	}
	if (valid) {
		log.debug "Alert: ${action}, Value: ${value}"
		sendEvent(name: "alert", value: action)
		"st cmd 0x${device.deviceNetworkId} ${endpointId} 3 0x40 {${value} 00}"
	}
	else { log.debug "Invalid action" }
}

def setDirection() {
	def direction = (device.currentValue("loopDirection") == "Down" ? "Up" : "Down")
    log.trace direction
	sendEvent(name: "loopDirection", value: direction)
	if (device.currentValue("loopActive") == "Active") {
		def dirHex = (direction == "Down" ? "00" : "01")
        log.trace dirHex
		"st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {02 01 ${dirHex} 0000 0000}"
	}
}

def setLoopTime(value) {
	sendEvent(name:"loopTime", value: value)
	if (device.currentValue("loopActive") == "Active") {
		def finTime = swapEndianHex(hexF(value, 4))
		"st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {04 01 00 ${finTime} 0000}"
	}
}

def startLoop(Map params) {
	// direction either increments or decrements the hue value: "Up" will increment, "Down" will decrement
	def direction = (device.currentValue("loopDirection") != null ? (device.currentValue("loopDirection") == "Down" ? "00" : "01") : "00")
	log.trace direction
    	if (params?.direction != null) {
		direction = (params.direction == "Down" ? "00" : "01")
		sendEvent(name: "loopDirection", value: params.direction )
	}
	log.trace direction
	
	// time parameter is the time in seconds for a full loop
	def cycle = (device.currentValue("loopTime") != null ? device.currentValue("loopTime") : 2)
	log.trace cycle
    	if (params?.time != null) {
		cycle = params.time
		sendEvent(name:"loopTime", value: cycle)
	}
	log.trace cycle
    	def finTime = swapEndianHex(hexF(cycle, 4))
	log.trace finTime
	
	def cmds = []
	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
    cmds << "delay 200"
    	
	sendEvent(name: "switchColor", value: "Color Loop", displayed: false)
    sendEvent(name: "loopActive", value: "Active")
    	
	if (params?.hue != null) {  
		
		// start hue was specified, so convert to enhanced hue and start loop from there
		def sHue = Math.min(Math.round(params.hue * 255 / 100), 255)
		finHue = swapEndianHex(hexF(sHue, 4))
		log.debug "activating color loop from specified hue"
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {0F 01 ${direction} ${finTime} ${sHue}}"
        
	}
	else {
		       
        // start hue was not specified, so start loop from current hue updating direction and time
		log.debug "activating color loop from current hue"
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {07 02 ${direction} ${finTime} 0000}"
		
	}
	cmds
}

def stopLoop() {
	
	log.debug "deactivating color loop"
	def cmds = [
		"st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x44 {01 00 00 0000 0000}", "delay 200",
        "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0", "delay 200",
		"st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1", "delay 200",
		"st rattr 0x${device.deviceNetworkId} ${endpointId} 8 0", "delay 200",
        "st rattr 0x${device.deviceNetworkId} 0x0B 8 0"
        ]
	sendEvent(name: "loopActive", value: "Inactive")
	
	cmds
	
}

def whiteOn() {
	sendEvent(name: "whiteSwitch", value: "on")
    "st cmd 0x${device.deviceNetworkId} 0x0B 6 1 {}"
}

def whiteOff() {
	sendEvent(name: "whiteSwitch", value: "off")
    "st cmd 0x${device.deviceNetworkId} 0x0B 6 0 {}"
}

def setWhiteLevel(value, duration = 20) {
    log.trace "setWhiteLevel($value)"
    def transitionTime = swapEndianHex(hexF(duration,4))
    
	def cmds = []

	sendEvent(name: "level", value: value)
	def level = hex(value * 2.55)
    if(value == 1) { level = hex(1) }
	cmds << "st cmd 0x${device.deviceNetworkId} 0x0B 8 4 {${level} ${transitionTime}}"

	//log.debug cmds
	cmds
}

def setHue(value, duration = 20) {
	def max = 0xfe
    def transitionTime = swapEndianHex(hexF(duration,4))
    
  // log.trace "setHue($value)"
	sendEvent(name: "hue", value: value)
	def scaledValue = Math.round(value * max / 100.0)
	def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x00 {${hex(scaledValue)} 00 ${transitionTime}}"
	//log.info cmd
	cmd
}

def setAdjustedColor(value, duration = 20) {
   // log.debug "setAdjustedColor: ${value}"
	def adjusted = value + [:]
	adjusted.level = null // needed because color picker always sends 100
	setColor(adjusted, duration)
}

def setColor(value, duration = 20){
    log.trace "setColor($value)"
    
    def transitionTime = swapEndianHex(hexF(duration,4))
    
    def max = 0xfe
	if (value.hue == 0 && value.saturation == 0) { setColorTemperature(3500) }
    else if (value.red == 255 && value.blue == 185 && value.green == 255) { setColorTemperature(2700) }
    else {
    if (value.hex) { sendEvent(name: "color", value: value.hex, displayed:false)}

    def colorName = getColorName(value.hue)
    sendEvent(name: "colorName", value: colorName)
    sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)

    log.debug "color name is : $colorName"
    sendEvent(name: "hue", value: value.hue, displayed:false)
    sendEvent(name: "saturation", value: value.saturation, displayed:false)
    def scaledHueValue = evenHex(Math.round(value.hue * max / 100.0))
    def scaledSatValue = evenHex(Math.round(value.saturation * max / 100.0))

    def cmd = []
    if (value.switch != "off" && device.latestValue("switch") == "off") {
        cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
        cmd << "delay 150"
    }

    cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x06 {${scaledHueValue} ${scaledSatValue} ${transitionTime}}"
    
    if (value.level) {
        state.levelValue = value.level
        sendEvent(name: "level", value: value.level)
        def level = hex(value.level * 255 / 100)
        cmd << "delay 200"
        cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} ${transitionTime}}"
    }
    
    if (value.switch == "off") {
        cmd << "delay 150"
        cmd << off()
    }

    cmd
    }
}

def setSaturation(value, duration = 20) {
	
    def transitionTime = swapEndianHex(hexF(duration,4))
	def max = 0xfe
   // log.trace "setSaturation($value)"
	sendEvent(name: "saturation", value: value)
	def scaledValue = Math.round(value * max / 100.0)
	def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x03 {${hex(scaledValue)} ${transitionTime}}"
	//log.info cmd
	cmd
}

def refresh() {
   
// Ping the device with color as to not get out of sync 
    [
	"st rattr 0x${device.deviceNetworkId} ${endpointId} 6 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 8 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} 0x0B 8 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1","delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0","delay 500",
    "st wattr 0x${device.deviceNetworkId} ${endpointId} 8 0x10 0x21 {0015}"
    
    ]
}

def poll(){
	log.debug "Poll is calling refresh"
	refresh()
}

def configure(){
	log.debug "Initiating configuration reporting and binding"
    
    [  
    	zigbee.configSetup("6","0","0x10","0","60","{}"), "delay 1000",
        zigbee.configSetup("8","0","0x20","5","600","{10}"), "delay 1500",
        
        "zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 6 {${device.zigbeeId}} {}", "delay 1000",
		"zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 8 {${device.zigbeeId}} {}", "delay 1000",
        "zdo bind 0x${device.deviceNetworkId} 0x0B 1 8 {${device.zigbeeId}} {}", "delay 1000",
        "zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 0x0300 {${device.zigbeeId}} {}"
	]
    
}

//input Hue Integer values; returns color name for saturation 100%
private getColorName(hueValue){
    if(hueValue>360 || hueValue<0)
        return

    hueValue = Math.round(hueValue / 100 * 360)

    log.debug "hue value is $hueValue"

    def colorName = "Color Mode"
    if(hueValue>=0 && hueValue <= 4){
        colorName = "Red"
    }
    else if (hueValue>=5 && hueValue <=21 ){
        colorName = "Brick Red"
    }
    else if (hueValue>=22 && hueValue <=30 ){
        colorName = "Safety Orange"
    }
    else if (hueValue>=31 && hueValue <=40 ){
        colorName = "Dark Orange"
    }
    else if (hueValue>=41 && hueValue <=49 ){
        colorName = "Amber"
    }
    else if (hueValue>=50 && hueValue <=56 ){
        colorName = "Gold"
    }
    else if (hueValue>=57 && hueValue <=65 ){
        colorName = "Yellow"
    }
    else if (hueValue>=66 && hueValue <=83 ){
        colorName = "Electric Lime"
    }
    else if (hueValue>=84 && hueValue <=93 ){
        colorName = "Lawn Green"
    }
    else if (hueValue>=94 && hueValue <=112 ){
        colorName = "Bright Green"
    }
    else if (hueValue>=113 && hueValue <=135 ){
        colorName = "Lime"
    }
    else if (hueValue>=136 && hueValue <=166 ){
        colorName = "Spring Green"
    }
    else if (hueValue>=167 && hueValue <=171 ){
        colorName = "Turquoise"
    }
    else if (hueValue>=172 && hueValue <=187 ){
        colorName = "Aqua"
    }
    else if (hueValue>=188 && hueValue <=203 ){
        colorName = "Sky Blue"
    }
    else if (hueValue>=204 && hueValue <=217 ){
        colorName = "Dodger Blue"
    }
    else if (hueValue>=218 && hueValue <=223 ){
        colorName = "Navy Blue"
    }
    else if (hueValue>=224 && hueValue <=251 ){
        colorName = "Blue"
    }
    else if (hueValue>=252 && hueValue <=256 ){
        colorName = "Han Purple"
    }
    else if (hueValue>=257 && hueValue <=274 ){
        colorName = "Electric Indigo"
    }
    else if (hueValue>=275 && hueValue <=289 ){
        colorName = "Electric Purple"
    }
    else if (hueValue>=290 && hueValue <=300 ){
        colorName = "Orchid Purple"
    }
    else if (hueValue>=301 && hueValue <=315 ){
        colorName = "Magenta"
    }
    else if (hueValue>=316 && hueValue <=326 ){
        colorName = "Hot Pink"
    }
    else if (hueValue>=327 && hueValue <=335 ){
        colorName = "Deep Pink"
    }
    else if (hueValue>=336 && hueValue <=339 ){
        colorName = "Raspberry"
    }
    else if (hueValue>=340 && hueValue <=352 ){
        colorName = "Crimson"
    }
    else if (hueValue>=353 && hueValue <=360 ){
        colorName = "Red"
    }

    colorName
}

// adding duration to enable transition time adjustments
def setLevel(value, duration = 21) {
	log.trace "setLevel($value)"
    def transitionTime = swapEndianHex(hexF(duration,4))
    
	def cmds = []

	if (value == 0) {
		sendEvent(name: "switch", value: "off")
        sendEvent(name: "switchColor", value: "off", displayed: false)
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
	}
	else if (device.currentValue("switch") == "off") {
		sendEvent(name: "switch", value: "on")
        sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)
	}

	sendEvent(name: "level", value: value)
	def level = hex(value * 2.55)
    if(value == 1) { level = hex(1) }
	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} ${transitionTime}}"

	//log.debug cmds
	cmds
}

private getEndpointId() {
	new BigInteger(device.endpointId, 16).toString()
}

private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

private evenHex(value){
    def s = new BigInteger(Math.round(value).toString()).toString(16)
    while (s.size() % 2 != 0) {
        s = "0" + s
    }
    s
}

//Need to reverse array of size 2
private byte[] reverseArray(byte[] array) {
    byte tmp;
    tmp = array[1];
    array[1] = array[0];
    array[0] = tmp;
    return array
}

private hexF(value, width) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

private String swapEndianHex(String hex) {
    reverseArray(hex.decodeHex()).encodeHex()
}

private Integer convertHexToInt(hex) {
	Integer.parseInt(hex,16)
}

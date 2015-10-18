/**
 *  Improved Zigbee Hue RGBW Bulb MultiAttribute
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
	definition (name: "Improved Zigbee Hue Bulb MA", namespace: "smartthings", author: "SmartThings") {
		capability "Switch Level"
		capability "Actuator"
		capability "Color Control"
        capability "Color Temperature"
		capability "Switch"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
       
        command "setAdjustedColor"
        
        // This is a new temporary counter to keep track of no responses
        attribute "unreachable", "number"
        attribute "colorMode", "string"
        attribute "colorName", "string"
        attribute "switchColor", "string"

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
			}
			tileAttribute ("device.colorTemperature", key: "SLIDER_CONTROL") {
            	attributeState "colorTemperature", action: "color temperature.setColorTemperature"
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
		controlTile("rgbSelector", "device.color", "color", height: 2, width: 2, inactiveLabel: false) {
			state "color", action:"setAdjustedColor"
		}
		controlTile("levelSliderControl", "device.level", "slider", height: 2, width: 4, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
		valueTile("level", "device.level", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "level", label: 'Level ${currentValue}%'
		}
		controlTile("saturationSliderControl", "device.saturation", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "saturation", action:"color control.setSaturation"
		}
		valueTile("saturation", "device.saturation", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "saturation", label: 'Sat ${currentValue}    '
		}
		controlTile("hueSliderControl", "device.hue", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "hue", action:"color control.setHue"
		}
        controlTile("colorTempSliderControl", "device.colorTemperature", "slider", height: 1, width: 2, inactiveLabel: false, range:"(2000..6500)") {
            state "colorTemperature", action:"color temperature.setColorTemperature"
        }
        valueTile("colorTemp", "device.colorTemperature", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
            state "colorTemperature", label: '${currentValue} K'
        }
        valueTile("colorName", "device.colorName", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
            state "colorName", label: '${currentValue}'
        }
        valueTile("colorMode", "device.colorMode", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
            state "colorMode", label: '${currentValue}'
        }
		standardTile("configure", "device.switch", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"configuration.configure", icon:"st.secondary.configure"
		}
        
        
		main(["switch"])
		details(["switch", "levelSliderControl", "colorName", "colorTemp", "refresh",  "configure"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
//log.info "description is $description"
    
    sendEvent(name: "unreachable", value: 0)
    
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
            if(!(description?.startsWith("catchall: 0104 0300"))){
                def result = createEvent(name: "switch", value: "off")
                sendEvent(name: "switchColor", value: "off", displayed: false)
                log.debug "Parse returned ${result?.descriptionText}"
                return result
            }
        }
    }
    else if (description?.startsWith("read attr -")) {
        def descMap = parseDescriptionAsMap(description)
        log.trace "descMap : $descMap"

        if (descMap.cluster == "0300") {
            if(descMap.attrId == "0000"){  //Hue Attribute
                def hueValue = Math.round(convertHexToInt(descMap.value) / 255 * 360)
                log.debug "Hue value returned is $hueValue"
                def colorName = getColorName(hueValue)
    		sendEvent(name: "colorName", value: colorName)
                if (device.currentValue("switch") == "on") { sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false) }
                sendEvent(name: "hue", value: hueValue, displayed:false)
            }
            else if(descMap.attrId == "0001"){ //Saturation Attribute
                def saturationValue = Math.round(convertHexToInt(descMap.value) / 255 * 100)
                log.debug "Saturation from refresh is $saturationValue"
                sendEvent(name: "saturation", value: saturationValue, displayed:false)
            }
            else if( descMap.attrId == "0007") {
                def tempInMired = convertHexToInt(descMap.value)
            	def tempInKelvin = Math.round(1000000/tempInMired)
            	sendEvent(name: "colorTemperature", value: tempInKelvin)
                log.debug "Parse returned ${result?.descriptionText}"
            	return result
            }
            else if( descMap.attrId == "0008") {
                sendEvent(name: "colorMode", value: (descMap.value == "02" ? "White" : "Color"))
                if (device.currentValue("switch") == "on") {
                	log.debug device.currentValue("switch")
                	sendEvent(name: "switchColor", value: (descMap.value == "02" ? "White" : device.currentValue("colorName")), displayed: false)
                }
                log.debug "Parse returned ${result?.descriptionText}"
                return result
            }
        }
        else if(descMap.cluster == "0008"){
            def dimmerValue = Math.round(convertHexToInt(descMap.value) * 100 / 255)
            log.debug "dimmer value is $dimmerValue"
            sendEvent(name: "level", value: dimmerValue)
        }
    }
    else {
        def name = description?.startsWith("on/off: ") ? "switch" : null
        if (name == "switch") {
        	def value = (description?.endsWith(" 1") ? "on" : "off")
        	sendEvent(name: "switchColor", value: (value == "off" ? "off" : device.currentValue("colorName")), displayed: false)
        }
        else { def value = null }
        def result = createEvent(name: name, value: value)
        log.debug "Parse returned ${result?.descriptionText}"
        return result
    }

}

def parseDescriptionAsMap(description) {
    (description - "read attr - ").split(",").inject([:]) { map, param ->
        def nameAndValue = param.split(":")
        map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
    }
}

def on() {
	// just assume it works for now
	log.debug "on()"
	sendEvent(name: "switch", value: "on")
    sendEvent(name: "switchColor", value: device.currentValue("colorName"), displayed: false)
	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
}

def off() {
	// just assume it works for now
	log.debug "off()"
	sendEvent(name: "switch", value: "off")
    sendEvent(name: "switchColor", value: "off", displayed: false)
	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
}

def setColorTemperature(value) {
    if(value<101){
        value = (value*38) + 2700		//Calculation of mapping 0-100 to 2700-6500
    }

    def tempInMired = Math.round(1000000/value)
    def finalHex = swapEndianHex(hexF(tempInMired, 4))
   // def genericName = getGenericName(value)
   // log.debug "generic name is : $genericName"

    def cmds = []
    sendEvent(name: "colorTemperature", value: value, displayed:false)
    sendEvent(name: "colorMode", value: "White")
    sendEvent(name: "switchColor", value: "White", displayed: false)
   // sendEvent(name: "colorName", value: genericName)

    cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x0300 0x0a {${finalHex} 2000}"

    cmds
}

def setHue(value) {
	def max = 0xfe
  // log.trace "setHue($value)"
	sendEvent(name: "hue", value: value)
	def scaledValue = Math.round(value * max / 100.0)
	def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x00 {${hex(scaledValue)} 00 2000}"
	//log.info cmd
	cmd
}

def setAdjustedColor(value) {
   // log.debug "setAdjustedColor: ${value}"
	def adjusted = value + [:]
	adjusted.level = null // needed because color picker always sends 100
	setColor(adjusted)
}

def setColor(value){
    log.trace "setColor($value)"
    def max = 0xfe
	if (value.hue == 0 && value.saturation == 0) { setColorTemperature(3500) }
    else if (value.red == 255 && value.blue == 185 && value.green == 255) { setColorTemperature(2700) }
    else {
    if (value.hex) { sendEvent(name: "color", value: value.hex, displayed:false)}

    def colorName = getColorName(value.hue)
    sendEvent(name: "colorName", value: colorName)
    sendEvent(name: "colorMode", value: "Color")
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

    cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x06 {${scaledHueValue} ${scaledSatValue} 2000}"
    
    if (value.level) {
        state.levelValue = value.level
        sendEvent(name: "level", value: value.level)
        def level = hex(value.level * 255 / 100)
        cmd << zigbeeSetLevel(level)
    }
    
    if (value.switch == "off") {
        cmd << "delay 150"
        cmd << off()
    }

    cmd
    }
}

def setSaturation(value) {

	def max = 0xfe
   // log.trace "setSaturation($value)"
	sendEvent(name: "saturation", value: value)
	def scaledValue = Math.round(value * max / 100.0)
	def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x03 {${hex(scaledValue)} 2000}"
	//log.info cmd
	cmd
}

def refresh() {

    def unreachable = device.currentValue("unreachable")
    if(unreachable == null) { 
    	sendEvent(name: 'unreachable', value: 1)
    }
    else { 
    	sendEvent(name: 'unreachable', value: unreachable + 1)
    }
    if(unreachable > 2) { 
    	sendEvent(name: "switch", value: "off")
        sendEvent(name: "switchColor", value: "off", displayed: false)
    }
    
// Ping the device with color as to not get out of sync 
    [
	"st rattr 0x${device.deviceNetworkId} ${endpointId} 6 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 8 0", "delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 1","delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 0","delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 7","delay 500",
    "st rattr 0x${device.deviceNetworkId} ${endpointId} 0x0300 8"
    
    ]
}

def poll(){
	log.debug "Poll is calling refresh"
	refresh()
}

def configure(){
	log.debug "Initiating configuration reporting and binding"
    
    [  
    	"zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 6 {${device.zigbeeId}} {}", "delay 1000",
		"zdo bind 0x${device.deviceNetworkId} ${endpointId} 1 8 {${device.zigbeeId}} {}", "delay 1000",
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

def setLevel(value) {
	log.trace "setLevel($value)"
    
    def unreachable = device.currentValue("unreachable")
    log.debug unreachable
    if(unreachable == null) { 
    	sendEvent(name: 'unreachable', value: 1)
    }
    else { 
    	sendEvent(name: 'unreachable', value: unreachable + 1)
    }
    if(unreachable > 2) { 
    	sendEvent(name: "switch", value: "off")
        sendEvent(name: "switchColor", value: "off", displayed: false)
    }
    
	def cmds = []

	if (value == 0) {
		sendEvent(name: "switch", value: "off")
        sendEvent(name: "switchColor", value: "off", displayed: false)
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
	}
	else if (device.currentValue("switch") == "off" && unreachable < 2) {
		sendEvent(name: "switch", value: "on")
        log.debug device.currentValue("colorMode")
        sendEvent(name: "switchColor", value: (device.currentValue("colorMode") == "White" ? "White" : device.currentValue("colorName")), displayed: false)
	}

	sendEvent(name: "level", value: value)
	def level = hex(value * 2.55)
    if(value == 1) { level = hex(1) }
	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} 1500}"

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

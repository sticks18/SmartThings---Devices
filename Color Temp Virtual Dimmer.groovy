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
 *  Color Temp Virtual Dimmer - to be used with Color Temp via Virtual Dimmer SmartApp to make color temp adjustments in common SmartApps
 *
 *  Author: Scott Gibson
 *
 *  Date: 2015-03-12
 */
metadata {
	definition (name: "Color Temp Virtual Dimmer", namespace: "sticks18", author: "Scott Gibson") {
		capability "Actuator"
        capability "Switch Level"
        capability "Switch"
        
        command "updateTemp"
        
        attribute "kelvin", "number"
	
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
    	valueTile("kelvin", "device.kelvin") {
			state("device.kelvin", label:'${currentValue}k',
				backgroundColors:[
					[value: 2900, color: "#FFA757"],
					[value: 3300, color: "#FFB371"],
					[value: 3700, color: "#FFC392"],
					[value: 4100, color: "#FFCEA6"],
					[value: 4500, color: "#FFD7B7"],
					[value: 4900, color: "#FFE0C7"],
					[value: 5300, color: "#FFE8D5"],
                    [value: 6600, color: "#FFEFE1"]
				]
			)
		}
        valueTile("level", "device.level", inactiveLabel: false, decoration: "flat") {
			state "level", label: 'Level ${currentValue}%'
		}
        controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 2, inactiveLabel: false) {
			state "level", action:"switch level.setLevel"
		}
		main "kelvin"
		details "levelSliderControl", "level", "kelvin"
	}
}

def parse(String description) {
}

def setLevel(value) {
    log.debug "Setting level to ${value}"
    sendEvent(name: "level", value: value)
    parent.setLevel(this, value)
}

def on() {
}

def off() {
}

def updateTemp(value) {
	sendEvent(name: "kelvin", value: value)
}

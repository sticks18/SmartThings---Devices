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
/* Philips Hue Wireless Dimmer

Capabilities:
  Actuator
  Configuration
  Button
  Sensor
  
    
*/

metadata {
	definition (name: "Hue Dimmer Button Controller", namespace: "Sticks18", author: "Scott G") {
		capability "Actuator"
		capability "Button"
		capability "Configuration"
		capability "Sensor"

		// fingerprint specific to Hue remote taken from Basic Cluster.
		fingerprint profileId: "C05E", inClusters: "0000", outClusters: "0000,0003,0004,0006,0008", manufacturer: "Philips", model: "RWL020"
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
		main "button"
		details(["button"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
	// next line is debugging code to see raw zigbee message. no longer needed
	// log.trace description
    
    // Create a map from the raw zigbee message to make parsing more intuitive
    def msg = zigbee.parse(description)
    
    // Check if the message comes from the on/off cluster (0x0006 in zigbee) to determine if button was On or Off
    if (msg.clusterId == 6) {
    	
    	// Command 1 is the zigbee 'on' command, so make that button 1. Else it must be 'off' command, make that button 4.
    	// Then create the button press event. All button events with be of type "pushed", not "held".
    	def button = (msg.command == 1 ? 1 : 4)
        def result = createEvent(name: "button", value: "pushed", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true)
        log.debug "Parse returned ${result?.descriptionText}"
        return result
  
    }
    
    // Check if message comes from the level cluster (0x0008 in zigbee) to determine if button was dim up/down
    if (msg.clusterId == 8) {
    	
    	// Remote sends move level and stop commands when dim up/down pushed or held
    	// Additional parsing and debugging created for when buttons are held, but not creating events for them
    	// since it is difficult to separate them from pushed events
    	switch (msg.command) 
        {
        	// Move level command means dim/up down was pushed or held
        	case 2:
            	
            	// Position -6 and -5 is the two digit hex of the move step size, which is different for an initial push vs a hold
            	// Check it for the initial push value '1E'
                def y = description[-6..-5]
                if (y == "1E") {
            		
            	    // Determine the button push by looking at the move direction determined in position -8 and -7
            	    // "00" means to move up in level, so dim up button was pushed. Create button event
                    def button = (description[-8..-7] == "00" ? 2 : 3)
                	def result = createEvent(name: "button", value: "pushed", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true)
                    log.debug "Parse returned ${result?.descriptionText}"
                    return result
					break
                }
                // If move step size is not '1E' then the button is being held. Send debug message, but take no action
                log.debug "Received held message"
                break
         	
            case 3:
            	// If level command is 3, then stop moving command was sent. Send debug message, but take no action.
            	// This stop command could be used to get a button 'held' event, but we would need to ignore the initial button push
            	log.debug "Received stop message"
                break
                
        }
     }   
    
}


// Configuration runs during device join
def configure() {

	log.debug "configure"
	String zigbeeId = swapEndianHex(device.hub.zigbeeId)
	log.debug "Confuguring Reporting and Bindings."
	def configCmds = [	
		
		// Bind the outgoing on/off cluster from remote to hub, so remote sends hub messages when On/Off buttons pushed
        	"zdo bind 0x${device.deviceNetworkId} 1 1 6 {${device.zigbeeId}} {}", "delay 1000",
		
		// Bind the outgoing level cluster from remote to hub, so remote sends hub messages when Dim Up/Down buttons pushed
		"zdo bind 0x${device.deviceNetworkId} 1 1 8 {${device.zigbeeId}} {}", "delay 500",
        
	]
    return configCmds 

   
}

private getEndpointId() {
	new BigInteger(device.endpointId, 16).toString()
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

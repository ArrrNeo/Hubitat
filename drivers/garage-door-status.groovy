/**
 *  ****************  Garage Door Status  ****************
 *
 *  Design Usage:
 *  This driver is designed to Garage Door Status and will connect to a MQTT broker.
 *
 *  Copyright 2020 Naveen Rawat
 *
 *  This driver is free and you may do as you like with it. The driver follows template of following code:
 *  https://raw.githubusercontent.com/PrayerfulDrop/Hubitat/master/drivers/Salt%20Tank.groovy and has
 *  following copyright
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *  ****************  Salt Tank MQTT Driver  ****************
 *
 *  Design Usage:
 *  This driver is designed to show dynamic icons of salt water tank and will connect to a MQTT broker.
 *
 *  Copyright 2019 Aaron Ward
 *
 *  This driver is free and you may do as you like with it.
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 * ------------------------------------------------------------------------------------------------------------------------------
 *
 *
 *  Changes:
 *
 *  1.0.0 - Garage Door through MQTT
 */

metadata {
    definition (name: "Garage Door", namespace: "neo", author: "Naveen Rawat", importURL: "https://raw.githubusercontent.com/ArrrNeo/Hubitat/master/drivers/garage-door-status.groovy") {
        capability "Initialize"
        capability "GarageDoorControl"
        capability "Switch"
        capability "Actuator"
        command "open"
        command "close"
        command "on"
        command "off"
        command "publishMsg", ["String"]
        attribute "door", "ENUM", ["unknown", "open", "closing", "closed", "opening"]
        attribute "switch","ENUM", ["on","off"]
       }

    preferences {
        input name: "Garage Door", type: "text", title: "Garage Door:", required: true, displayDuringSetup: true, defaultValue: "close"
        input name: "MQTTBroker", type: "text", title: "ip addr of broker", required: true, displayDuringSetup: true
        input name: "username", type: "text", title: "MQTT Username:", description: "", required: false, displayDuringSetup: true
        input name: "password", type: "password", title: "MQTT Password:", description: "", required: false, displayDuringSetup: true
        input name: "topicSub", type: "text", title: "Topic to Subscribe:", description: "", required: false, displayDuringSetup: true
        input name: "topicPub", type: "text", title: "Topic to Publish:", description: "", required: false, displayDuringSetup: true
    }
}


def installed() {
    log.info "installed..."
}

// Parse incoming device messages to generate events
def parse(String description) {
    Date date = new Date(); 
    topic = interfaces.mqtt.parseMessage(description).topic
    topic = topic.substring(topic.lastIndexOf("/") + 1)
    payload = interfaces.mqtt.parseMessage(description).payload

    log.debug topic
    log.debug payload

    sendEvent(name: "${topic}", value: "${payload}", displayed: true)
    sendEvent(name: "Last Payload Received", value: "Topic: ${topic} - ${date.toString()}", displayed: true)
    state."${topic}" = "${payload}"
    state.lastpayloadreceived = "Topic: ${topic} : ${payload} - ${date.toString()}"

    log.debug "Garage Door Status: ${payload}"

    if("${payload}" == "close") {
        sendEvent(name: "door", value: "closed", isStateChange: true)
    } else {
        sendEvent(name: "door", value: "open", isStateChange: true)
    }
}

def publishMsg(String s) {
    log.debug "Sent this: ${s} to ${settings?.topicPub}"
    interfaces.mqtt.publish(settings?.topicPub, s)
}

def close() {
    sendEvent(name: "door", value: "closing")
    interfaces.mqtt.publish(settings?.topicPub, "toggle")
    interfaces.mqtt.publish(settings?.topicPub, "getstatus")
}

def open() {
    sendEvent(name: "door", value: "opening")
    interfaces.mqtt.publish(settings?.topicPub, "toggle")
    interfaces.mqtt.publish(settings?.topicPub, "getstatus")
}

def on() {
    open()
}

def off() {
    close()
}


//def updated() {
//    log.info "Updated..."
//    initialize()
//}

def uninstalled() {
    log.info "Disconnecting from mqtt"
    interfaces.mqtt.disconnect()
}

def initialize() {
    mqttConnect()
}

def mqttConnect() {
    try {
        //open connection
        mqttbroker = "tcp://" + settings?.MQTTBroker + ":1883"
        interfaces.mqtt.connect(mqttbroker, "hubitat-garagedoorstatus",
                                settings?.username, settings?.password)
        pauseExecution(1000)
        log.debug "Subscribed to: ${settings?.topicSub}"

        interfaces.mqtt.subscribe(settings?.topicSub)
    } catch(e) {
        log.debug "Initialize error: ${e.message}"
    }
}

//def mqttClientStatus(String status) {
//    if(!status.contains("succeeded")) {
//        try {
//            interfaces.mqtt.disconnect()
//        } catch (e) { }
//        log.debug "Broker: ${status} Will restart in 5 seconds"
//        runIn(5, mqttConnect)  
//    }
//}

//def logsOff() {
//   log.warn "Debug logging disabled."
//    device.updateSetting("logEnable",[value:"false",type:"bool"])
//}

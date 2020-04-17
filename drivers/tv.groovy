metadata {
    definition (name: "TV", namespace: "neo", author: "Naveen Rawat", importURL: "TBD") {
        capability "Initialize"
        capability "SamsungTV"

        attribute "messageButton",   "JSON_OBJECT"
        attribute "mute",            "ENUM",    ["muted", "unknown", "unmuted"]
        attribute "pictureMode",     "ENUM",    ["unknown", "standard", "movie", "dynamic"]
        attribute "soundMode",       "ENUM",    ["speech", "movie", "unknown", "standard", "music"]
        attribute "switch",          "ENUM",    ["on", "off"]
        attribute "volume",          "NUMBER"

        command "mute"
        command "off"
        command "on"
        command "setPictureMode",   ["ENUM"]
        command "setSoundMode",     ["ENUM"]
        command "setVolume",        ["NUMBER"]
        command "showMessage",      ["STRING", "STRING", "STRING", "STRING"]
        command "unmute"
        command "volumeDown"
        command "volumeUp"
    }

    preferences {
    }
}

def off() {
    log.info "off..."
    sendEvent(name: "switch", value: "off", isStateChange: true)
    state."switch" = "off"
}

def on() {
    log.info "on..."
    sendEvent(name: "switch", value: "on", isStateChange: true)
    state."switch" = "on"
}

def installed() {
    log.info "installed..."
}

def updated() {
    log.info "Updated..."
    initialize()
}

def uninstalled() {
    log.info "uninstalled..."
}

def initialize() {
    log.info "initialize..."
}

def volumeDown() {
    log.info "volumeDown..."
}

def volumeUp() {
    log.info "volumeUp..."
}

def mute() {
    log.info "mute..."
    sendEvent(name: "mute", value: "muted", isStateChange: true)
    state."mute" = "muted"
}

def setPictureMode(Enum en) {
    log.info "setPictureMode..."
}

def setSoundMode(Enum en) {
    log.info "setSoundMode..."
}

def setVolume(Number num) {
    log.info "setVolume..."
}

def showMessage(String str1, String str2, String str3, String str4) {
    log.info "showMessage..."
}

def unmute() {
    log.info "unmute..."
    sendEvent(name: "mute", value: "unmuted", isStateChange: true)
    state."mute" = "unmuted"
}

metadata {
    definition (name: "Simple Switch", namespace: "neo", author: "Naveen Rawat", importURL: "TBD") {
        capability "Initialize"
        capability "Switch"
        command "off"
        command "on"
        attribute "switch", "ENUM", ["on", "off"]
    }

    preferences {
    }
}

def off() {
    log.info "off..."
}

def on() {
    log.info "on..."
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

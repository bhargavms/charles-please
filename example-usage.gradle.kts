// Example usage of the Charles Please plugin
plugins {
    id("dev.mogra.charlesplease")
}

// Configure the plugin
charlesPlease {
    // For manual proxy configuration
    host.set("10.0.0.5")
    port.set(8888)
    bypass.set("localhost,127.0.0.1,*.corp")
    serial.set("") // Optional: specific device serial

    // For PAC URL configuration
    // pacUrl.set("http://proxy.example/pacfile.pac")

    // For auto-discovery (use with charlesPleaseApply task)
    // autoDiscover.set(true)
}

// Example tasks:
// ./gradlew charlesPleaseApply  - Apply Charles proxy settings (auto-discovery or manual)
// ./gradlew charlesPleaseClear  - Clear Charles proxy settings

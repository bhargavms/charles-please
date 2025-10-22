package dev.mogra.charlesplease

import org.gradle.api.provider.Property

/**
 * Configuration extension for the Charles Please plugin.
 *
 * This extension allows you to configure proxy settings for Android devices via the `charlesPlease` block
 * in your build script. You can either manually specify proxy settings or enable auto-discovery to
 * automatically detect Charles Proxy running on your local machine.
 *
 * ## Manual Configuration Example
 *
 * ```kotlin
 * charlesPlease {
 *     host.set("192.168.1.100")
 *     port.set(8888)
 *     bypass.set("localhost,127.0.0.1,*.internal")
 *     serial.set("emulator-5554") // Optional: target specific device
 * }
 * ```
 *
 * ## Auto-Discovery Example
 *
 * ```kotlin
 * charlesPlease {
 *     autoDiscover.set(true)
 *     bypass.set("localhost,127.0.0.1")
 * }
 * ```
 *
 * ## PAC Configuration Example
 *
 * ```kotlin
 * charlesPlease {
 *     pacUrl.set("http://proxy.example.com/proxy.pac")
 * }
 * ```
 *
 * @see CharlesPleasePlugin
 */
abstract class CharlesPleaseExtension {
    /**
     * The proxy server host address.
     *
     * This should be an IP address or hostname that is accessible from the Android device or emulator.
     * For emulators, use the host machine's network IP address (not `localhost` or `127.0.0.1`).
     *
     * **Default:** `""` (empty)
     *
     * **Example:**
     * ```kotlin
     * host.set("192.168.1.100")
     * ```
     *
     * **Note:** Not required when [autoDiscover] is enabled.
     */
    abstract val host: Property<String>

    /**
     * The proxy server port number.
     *
     * Charles Proxy typically runs on port 8888 by default.
     *
     * **Default:** `8888`
     *
     * **Example:**
     * ```kotlin
     * port.set(8888)
     * ```
     */
    abstract val port: Property<Int>

    /**
     * Comma-separated list of hosts to bypass the proxy.
     *
     * Hosts in this list will connect directly without going through the proxy.
     * Supports wildcards (e.g., `*.internal`, `*.local`).
     *
     * **Default:** `""` (empty - no bypass)
     *
     * **Example:**
     * ```kotlin
     * bypass.set("localhost,127.0.0.1,*.corp.internal")
     * ```
     */
    abstract val bypass: Property<String>

    /**
     * PAC (Proxy Auto-Configuration) URL.
     *
     * When set, the device will use the PAC file at this URL to determine proxy settings.
     * This takes precedence over manual [host] and [port] configuration.
     *
     * **Default:** `""` (empty - PAC not used)
     *
     * **Example:**
     * ```kotlin
     * pacUrl.set("http://proxy.example.com/proxy.pac")
     * ```
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Proxy_servers_and_tunneling/Proxy_Auto-Configuration_PAC_file">PAC File Documentation</a>
     */
    abstract val pacUrl: Property<String>

    /**
     * Android device serial number.
     *
     * When specified, commands will target only the device with this serial number.
     * If empty, commands will target the only connected device, or fail if multiple devices are connected.
     *
     * **Default:** `""` (empty - auto-detect single device)
     *
     * **Example:**
     * ```kotlin
     * serial.set("emulator-5554")
     * ```
     *
     * **Tip:** Use `adb devices` to list connected device serial numbers.
     */
    abstract val serial: Property<String>

    /**
     * Enable automatic Charles Proxy discovery.
     *
     * When enabled, the plugin will automatically detect the local IP address and verify that
     * Charles Proxy is running on port 8888. This eliminates the need to manually configure
     * [host] and [port].
     *
     * **Default:** `false`
     *
     * **Example:**
     * ```kotlin
     * autoDiscover.set(true)
     * ```
     *
     * **Requirements:**
     * - Charles Proxy must be running on the host machine
     * - Charles Proxy must be listening on port 8888
     * - The host machine must be on the same network as the device/emulator
     */
    abstract val autoDiscover: Property<Boolean>

    init {
        // Set default values - only if the methods are available (for testing compatibility)
        try {
            host.convention("")
            port.convention(8888)
            bypass.convention("")
            pacUrl.convention("")
            serial.convention("")
            autoDiscover.convention(false)
        } catch (e: Exception) {
            // Ignore exceptions during initialization (e.g., in tests with mocks)
        }
    }
}

package dev.mogra.charlesplease

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Gradle task that clears Charles Proxy settings from a connected Android device or emulator.
 *
 * This task removes all proxy-related global settings from an Android device using ADB commands,
 * restoring the device to direct internet connectivity without a proxy.
 *
 * ## Usage
 *
 * Run the task from command line:
 * ```bash
 * ./gradlew charlesPleaseClear
 * ```
 *
 * Or reference it in your build script:
 * ```kotlin
 * tasks.named("charlesPleaseClear") {
 *     // Task configuration if needed
 * }
 * ```
 *
 * ## What Gets Cleared
 *
 * This task removes the following Android global settings:
 * - `http_proxy` - The proxy host:port setting
 * - `global_http_proxy_host` - The proxy host
 * - `global_http_proxy_port` - The proxy port
 * - `global_http_proxy_exclusion_list` - The bypass list
 * - `proxy_pac_url` - The PAC file URL
 *
 * ## Requirements
 *
 * - ADB (Android Debug Bridge) must be installed and available in PATH
 * - At least one Android device or emulator must be connected
 * - The device must be accessible via ADB
 *
 * @see CharlesPleaseExtension for configuration options
 * @see CharlesPleaseApplyTask for applying proxy settings
 */
abstract class CharlesPleaseClearTask : DefaultTask() {
    /**
     * The plugin extension containing device configuration.
     *
     * This property is automatically set by the plugin when the task is registered.
     * It's used to determine which device to target (via the [CharlesPleaseExtension.serial] property).
     */
    @get:Input
    abstract val extension: Property<CharlesPleaseExtension>

    /**
     * Executes the task to clear proxy settings.
     *
     * This method removes all proxy-related settings from the connected Android device
     * using ADB commands.
     *
     * @throws RuntimeException if no device is connected or ADB command fails
     */
    @TaskAction
    fun clearProxy() {
        val config = extension.get()
        val commandFactory = CommandFactory(logger, config)
        val commandInvoker = CommandExecutor(logger)

        val clearCommand = commandFactory.createClearProxyCommand()
        commandInvoker.execute(clearCommand)
    }
}

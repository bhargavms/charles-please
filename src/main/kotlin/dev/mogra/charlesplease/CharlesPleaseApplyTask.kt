package dev.mogra.charlesplease

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * Gradle task that applies Charles Proxy settings to a connected Android device or emulator.
 *
 * This task configures the global proxy settings on an Android device using ADB commands.
 * It supports both manual proxy configuration and automatic Charles Proxy discovery.
 *
 * ## Usage
 *
 * Run the task from command line:
 * ```bash
 * ./gradlew charlesPleaseApply
 * ```
 *
 * Or reference it in your build script:
 * ```kotlin
 * tasks.named("charlesPleaseApply") {
 *     // Task configuration if needed
 * }
 * ```
 *
 * ## Configuration
 *
 * The task uses settings from the [CharlesPleaseExtension] configured in the `charlesPlease` block:
 *
 * ```kotlin
 * charlesPlease {
 *     host.set("192.168.1.100")
 *     port.set(8888)
 *     bypass.set("localhost,127.0.0.1")
 * }
 * ```
 *
 * ## Requirements
 *
 * - ADB (Android Debug Bridge) must be installed and available in PATH
 * - At least one Android device or emulator must be connected
 * - The device must be accessible via ADB
 *
 * @see CharlesPleaseExtension for configuration options
 * @see CharlesPleaseClearTask for clearing proxy settings
 */
abstract class CharlesPleaseApplyTask : DefaultTask() {
    /**
     * The plugin extension containing proxy configuration.
     *
     * This property is automatically set by the plugin when the task is registered.
     */
    @get:Input
    abstract val extension: Property<CharlesPleaseExtension>

    /**
     * Executes the task to apply proxy settings.
     *
     * This method reads the configuration from [extension] and applies the proxy settings
     * to the connected Android device using ADB commands.
     *
     * @throws RuntimeException if no device is connected or ADB command fails
     */
    @TaskAction
    fun applyProxy() {
        val config = extension.get()
        val commandFactory = CommandFactory(logger, config)
        val commandInvoker = CommandExecutor(logger)

        val applyCommand = commandFactory.createApplyProxyCommand()
        commandInvoker.execute(applyCommand)
    }
}

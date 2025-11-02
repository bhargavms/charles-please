package dev.mogra.charlesplease

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Gradle plugin for automatically configuring Android device proxy settings via ADB.
 *
 * This plugin provides tasks to apply and clear Charles Proxy settings on Android devices
 * and emulators using ADB (Android Debug Bridge). It supports both manual proxy configuration
 * and automatic Charles Proxy discovery.
 *
 * ## Usage
 *
 * Apply the plugin in your `build.gradle.kts`:
 * ```kotlin
 * plugins {
 *     id("io.github.bhargavms.gradle.charlesplease")
 * }
 * ```
 *
 * Configure the plugin:
 * ```kotlin
 * charlesPlease {
 *     host.set("192.168.1.100")
 *     port.set(8888)
 *     autoDiscover.set(true)
 * }
 * ```
 *
 * ## Available Tasks
 *
 * - `charlesPleaseApply` - Apply Charles proxy settings to the connected Android device
 * - `charlesPleaseClear` - Clear Charles proxy settings from the connected Android device
 *
 * @see CharlesPleaseExtension for configuration options
 * @see CharlesPleaseApplyTask for applying proxy settings
 * @see CharlesPleaseClearTask for clearing proxy settings
 */
class CharlesPleasePlugin : Plugin<Project> {
    /**
     * Applies the plugin to the given project.
     *
     * This method creates the `charlesPlease` extension for configuration and registers
     * the `charlesPleaseApply` and `charlesPleaseClear` tasks.
     *
     * @param project The Gradle project to apply the plugin to
     */
    override fun apply(project: Project) {
        // Create extension for plugin configuration
        val extension = project.extensions.create("charlesPlease", CharlesPleaseExtension::class.java)

        // Register tasks
        project.tasks.register("charlesPleaseApply", CharlesPleaseApplyTask::class.java) { task ->
            task.group = "charles"
            task.description = "Apply Charles proxy settings to Android device via ADB"
            task.extension.set(extension)
        }

        project.tasks.register("charlesPleaseClear", CharlesPleaseClearTask::class.java) { task ->
            task.group = "charles"
            task.description = "Clear Charles proxy settings from Android device"
            task.extension.set(extension)
        }
    }
}

package dev.mogra.charlesplease

import org.gradle.api.logging.Logger
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * ADB command classes following the command pattern.
 */
internal class CheckDeviceConnectionCommand(
    private val logger: Logger,
    private val serial: String? = null,
) : Command {
    override fun execute() {
        try {
            executeAdbCommand("get-state")
        } catch (e: Exception) {
            logger.error("No device connected or ADB not available", e)
            throw RuntimeException("No Android device connected")
        }
    }

    private fun executeAdbCommand(vararg args: String): String {
        val command = mutableListOf("adb")

        if (!serial.isNullOrEmpty()) {
            command.addAll(listOf("-s", serial))
        }

        command.addAll(args)

        logger.info("Executing: ${command.joinToString(" ")}")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()

        val output = StringBuilder()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("ADB command failed with exit code: $exitCode")
        }

        return output.toString().trim()
    }
}

/**
 * Command to set a global Android setting.
 */
internal class SetGlobalSettingCommand(
    private val logger: Logger,
    private val serial: String?,
    private val key: String,
    private val value: String,
) : Command {
    override fun execute() {
        logger.debug("Setting global setting: $key = $value")
        executeAdbCommand("shell", "settings", "put", "global", key, value)
    }

    private fun executeAdbCommand(vararg args: String): String {
        val command = mutableListOf("adb")

        if (!serial.isNullOrEmpty()) {
            command.addAll(listOf("-s", serial))
        }

        command.addAll(args)

        logger.info("Executing: ${command.joinToString(" ")}")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()

        val output = StringBuilder()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("ADB command failed with exit code: $exitCode")
        }

        return output.toString().trim()
    }
}

/**
 * Command to delete a global Android setting.
 */
internal class DeleteGlobalSettingCommand(
    private val logger: Logger,
    private val serial: String?,
    private val key: String,
) : Command {
    override fun execute() {
        logger.debug("Deleting global setting: $key")
        try {
            executeAdbCommand("shell", "settings", "delete", "global", key)
        } catch (e: Exception) {
            logger.debug("Could not delete setting $key: ${e.message}")
        }
    }

    private fun executeAdbCommand(vararg args: String): String {
        val command = mutableListOf("adb")

        if (!serial.isNullOrEmpty()) {
            command.addAll(listOf("-s", serial))
        }

        command.addAll(args)

        logger.info("Executing: ${command.joinToString(" ")}")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()

        val output = StringBuilder()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("ADB command failed with exit code: $exitCode")
        }

        return output.toString().trim()
    }
}

/**
 * Command to get a global Android setting.
 */
internal class GetGlobalSettingCommand(
    private val logger: Logger,
    private val serial: String?,
    private val key: String,
) : Command {
    override fun execute() {
        val value = executeAdbCommand("shell", "settings", "get", "global", key)
        logger.info("$key = $value")
    }

    private fun executeAdbCommand(vararg args: String): String {
        val command = mutableListOf("adb")

        if (!serial.isNullOrEmpty()) {
            command.addAll(listOf("-s", serial))
        }

        command.addAll(args)

        logger.info("Executing: ${command.joinToString(" ")}")

        val processBuilder = ProcessBuilder(command)
        val process = processBuilder.start()

        val output = StringBuilder()
        BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
        }

        val exitCode = process.waitFor()
        if (exitCode != 0) {
            throw RuntimeException("ADB command failed with exit code: $exitCode")
        }

        return output.toString().trim()
    }
}

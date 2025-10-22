package dev.mogra.charlesplease

import org.gradle.api.logging.Logger

/**
 * Command to clear proxy settings from Android device using command pattern.
 */
internal class ClearProxyCommand(
    private val logger: Logger,
    private val config: CharlesPleaseExtension,
) : Command {
    override fun execute() {
        val commandExecutor = CommandExecutor(logger)
        val serial = config.serial.get()

        // Check device connection first
        commandExecutor.execute(CheckDeviceConnectionCommand(logger, serial))

        logger.info("Clearing Charles proxy settings...")

        val clearCommands =
            listOf(
                DeleteGlobalSettingCommand(logger, serial, "http_proxy"),
                DeleteGlobalSettingCommand(logger, serial, "global_http_proxy_host"),
                DeleteGlobalSettingCommand(logger, serial, "global_http_proxy_port"),
                DeleteGlobalSettingCommand(logger, serial, "global_http_proxy_exclusion_list"),
                DeleteGlobalSettingCommand(logger, serial, "proxy_pac_url"),
            )

        commandExecutor.executeChain(clearCommands)
        logger.info("Done.")
    }
}

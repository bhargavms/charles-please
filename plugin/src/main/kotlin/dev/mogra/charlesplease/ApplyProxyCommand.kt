package dev.mogra.charlesplease

import org.gradle.api.logging.Logger

/**
 * Command to apply proxy settings to Android device using command pattern.
 */
internal class ApplyProxyCommand(
    private val logger: Logger,
    private val config: CharlesPleaseExtension,
) : Command {
    override fun execute() {
        val commandExecutor = CommandExecutor(logger)
        val serial = config.serial.get()

        // Always check device connection first
        commandExecutor.execute(CheckDeviceConnectionCommand(logger, serial))

        val host: String
        val port: Int

        if (config.autoDiscover.get()) {
            // Auto-discover Charles proxy using command pattern
            logger.info("Auto-discovering Charles proxy...")
            val discoverCommand = DiscoverCharlesProxyCommand(logger)
            commandExecutor.execute(discoverCommand)
            host = discoverCommand.getDiscoveredHost()
            port = discoverCommand.getDiscoveredPort()
        } else {
            // Use manual configuration
            host = config.host.get()
            port = config.port.get()

            if (host.isEmpty()) {
                throw RuntimeException(
                    "Host is required when auto-discovery is disabled. " +
                        "Set host in charlesPlease {} block or enable autoDiscover.",
                )
            }
        }

        val bypass = config.bypass.get()
        val pacUrl = config.pacUrl.get()

        if (pacUrl.isNotEmpty()) {
            applyPacConfiguration(commandExecutor, serial, pacUrl)
        } else {
            applyStaticProxy(commandExecutor, serial, host, port, bypass)
        }

        logProxyStatus(commandExecutor, serial)
    }

    private fun applyPacConfiguration(
        commandExecutor: CommandExecutor,
        serial: String?,
        pacUrl: String,
    ) {
        logger.info("Setting PAC URL to: $pacUrl")
        commandExecutor.execute(SetGlobalSettingCommand(logger, serial, "proxy_pac_url", pacUrl))

        // Clear static proxy keys to avoid conflicts
        val clearCommands =
            listOf(
                DeleteGlobalSettingCommand(logger, serial, "http_proxy"),
                DeleteGlobalSettingCommand(logger, serial, "global_http_proxy_host"),
                DeleteGlobalSettingCommand(logger, serial, "global_http_proxy_port"),
                DeleteGlobalSettingCommand(logger, serial, "global_http_proxy_exclusion_list"),
            )
        commandExecutor.executeChain(clearCommands)
    }

    private fun applyStaticProxy(
        commandExecutor: CommandExecutor,
        serial: String?,
        host: String,
        port: Int,
        bypass: String,
    ) {
        logger.info("Setting global proxy to: $host:$port")

        val proxyCommands =
            listOf(
                SetGlobalSettingCommand(logger, serial, "http_proxy", "$host:$port"),
                SetGlobalSettingCommand(logger, serial, "global_http_proxy_host", host),
                SetGlobalSettingCommand(logger, serial, "global_http_proxy_port", port.toString()),
            )
        commandExecutor.executeChain(proxyCommands)

        if (bypass.isNotEmpty()) {
            logger.info("Exclusion list: $bypass")
            commandExecutor.execute(SetGlobalSettingCommand(logger, serial, "global_http_proxy_exclusion_list", bypass))
        }

        // Ensure PAC is cleared when using static proxy
        commandExecutor.execute(DeleteGlobalSettingCommand(logger, serial, "proxy_pac_url"))
    }

    private fun logProxyStatus(
        commandExecutor: CommandExecutor,
        serial: String?,
    ) {
        logger.info("Proxy applied.")
        logger.info("Verify:")

        val verifyCommands =
            listOf(
                GetGlobalSettingCommand(logger, serial, "http_proxy"),
                GetGlobalSettingCommand(logger, serial, "global_http_proxy_host"),
                GetGlobalSettingCommand(logger, serial, "global_http_proxy_port"),
                GetGlobalSettingCommand(logger, serial, "global_http_proxy_exclusion_list"),
            )
        commandExecutor.executeChain(verifyCommands)
    }
}

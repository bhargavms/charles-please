package dev.mogra.charlesplease

import org.gradle.api.logging.Logger

/**
 * Factory for creating proxy commands using command pattern.
 */
internal class CommandFactory(
    private val logger: Logger,
    private val config: CharlesPleaseExtension,
) {
    /**
     * Create an apply proxy command.
     */
    fun createApplyProxyCommand(): ApplyProxyCommand {
        return ApplyProxyCommand(logger, config)
    }

    /**
     * Create a clear proxy command.
     */
    fun createClearProxyCommand(): ClearProxyCommand {
        return ClearProxyCommand(logger, config)
    }
}

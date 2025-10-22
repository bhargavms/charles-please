package dev.mogra.charlesplease

import org.gradle.api.logging.Logger

/**
 * Command executor that can execute single commands or chains of commands.
 */
internal class CommandExecutor(private val logger: Logger) {
    private val commandHistory = mutableListOf<Command>()

    /**
     * Execute a single command.
     */
    fun execute(command: Command) {
        try {
            logger.debug("Executing command: ${command::class.simpleName}")
            command.execute()
            commandHistory.add(command)
            logger.debug("Command executed successfully")
        } catch (e: Exception) {
            logger.error("Command execution failed: ${e.message}", e)
            throw e
        }
    }

    /**
     * Execute a chain of commands in sequence.
     */
    fun executeChain(commands: List<Command>) {
        commands.forEach { command ->
            execute(command)
        }
    }

    /**
     * Get the command history.
     */
    fun getCommandHistory(): List<Command> = commandHistory.toList()

    /**
     * Clear the command history.
     */
    fun clearHistory() {
        commandHistory.clear()
    }
}

package dev.mogra.charlesplease

import org.gradle.api.logging.Logger
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for CommandExecutor.
 * Uses real CommandExecutor with test command implementations.
 */
class CommandExecutorIntegrationTest {
    @Test
    fun `should execute single command successfully`() {
        // Given
        val logger = mock<Logger>()
        val executor = CommandExecutor(logger)
        var executed = false
        val command =
            object : Command {
                override fun execute() {
                    executed = true
                }
            }

        // When
        executor.execute(command)

        // Then
        assertTrue(executed)
        assertEquals(1, executor.getCommandHistory().size)
    }

    @Test
    fun `should execute command chain successfully`() {
        // Given
        val logger = mock<Logger>()
        val executor = CommandExecutor(logger)
        val executionOrder = mutableListOf<Int>()

        val commands =
            listOf(
                object : Command {
                    override fun execute() {
                        executionOrder.add(1)
                    }
                },
                object : Command {
                    override fun execute() {
                        executionOrder.add(2)
                    }
                },
                object : Command {
                    override fun execute() {
                        executionOrder.add(3)
                    }
                },
            )

        // When
        executor.executeChain(commands)

        // Then
        assertEquals(listOf(1, 2, 3), executionOrder)
        assertEquals(3, executor.getCommandHistory().size)
    }

    @Test
    fun `should propagate command execution errors`() {
        // Given
        val logger = mock<Logger>()
        val executor = CommandExecutor(logger)
        val command =
            object : Command {
                override fun execute() {
                    throw RuntimeException("Test error")
                }
            }

        // When & Then
        assertThrows<RuntimeException> {
            executor.execute(command)
        }
    }

    @Test
    fun `should stop chain execution on first error`() {
        // Given
        val logger = mock<Logger>()
        val executor = CommandExecutor(logger)
        val executionOrder = mutableListOf<Int>()

        val commands =
            listOf(
                object : Command {
                    override fun execute() {
                        executionOrder.add(1)
                    }
                },
                object : Command {
                    override fun execute() {
                        executionOrder.add(2)
                        throw RuntimeException("Test error")
                    }
                },
                object : Command {
                    override fun execute() {
                        executionOrder.add(3)
                    }
                },
            )

        // When & Then
        assertThrows<RuntimeException> {
            executor.executeChain(commands)
        }

        // Should have executed first two commands, but only first one completed successfully
        assertEquals(listOf(1, 2), executionOrder)
        assertEquals(1, executor.getCommandHistory().size) // Only successful commands are in history
    }

    @Test
    fun `should maintain command history`() {
        // Given
        val logger = mock<Logger>()
        val executor = CommandExecutor(logger)
        val command1 =
            object : Command {
                override fun execute() {}
            }
        val command2 =
            object : Command {
                override fun execute() {}
            }

        // When
        executor.execute(command1)
        executor.execute(command2)

        // Then
        val history = executor.getCommandHistory()
        assertEquals(2, history.size)
        assertEquals(command1, history[0])
        assertEquals(command2, history[1])
    }

    @Test
    fun `should clear command history`() {
        // Given
        val logger = mock<Logger>()
        val executor = CommandExecutor(logger)
        val command =
            object : Command {
                override fun execute() {}
            }

        executor.execute(command)
        assertEquals(1, executor.getCommandHistory().size)

        // When
        executor.clearHistory()

        // Then
        assertEquals(0, executor.getCommandHistory().size)
    }
}

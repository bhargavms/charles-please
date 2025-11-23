package dev.mogra.charlesplease

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertNotNull

/**
 * Unit tests for ApplyProxyCommand.
 * Tests the command creation and execution logic with mocked dependencies.
 */
class ApplyProxyCommandTest {
    @Test
    fun `should throw exception when host is empty and auto-discover is disabled`() {
        // Given
        val logger = mockk<Logger>()
        val config = createTestConfig(host = "", autoDiscover = false)

        // When & Then
        assertThrows<RuntimeException> {
            val command = ApplyProxyCommand(logger, config)
            command.execute()
        }
    }

    @Test
    fun `should create command with manual configuration`() {
        // Given
        val logger = mockk<Logger>()
        val config =
            createTestConfig(
                host = "192.168.1.100",
                port = 8888,
                autoDiscover = false,
            )

        // When
        val command = ApplyProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    @Test
    fun `should create command with auto-discover enabled`() {
        // Given
        val logger = mockk<Logger>()
        val config =
            createTestConfig(
                host = "",
                port = 8888,
                autoDiscover = true,
            )

        // When
        val command = ApplyProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    @Test
    fun `should create command with PAC URL`() {
        // Given
        val logger = mockk<Logger>()
        val config =
            createTestConfig(
                host = "192.168.1.100",
                port = 8888,
                pacUrl = "http://proxy.example.com/proxy.pac",
            )

        // When
        val command = ApplyProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    @Test
    fun `should create command with bypass list`() {
        // Given
        val logger = mockk<Logger>()
        val config =
            createTestConfig(
                host = "192.168.1.100",
                port = 8888,
                bypass = "localhost,127.0.0.1",
            )

        // When
        val command = ApplyProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    @Test
    fun `should create command with device serial`() {
        // Given
        val logger = mockk<Logger>()
        val config =
            createTestConfig(
                host = "192.168.1.100",
                port = 8888,
                serial = "emulator-5554",
            )

        // When
        val command = ApplyProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    private fun createTestConfig(
        host: String = "192.168.1.100",
        port: Int = 8888,
        bypass: String = "",
        pacUrl: String = "",
        serial: String = "",
        autoDiscover: Boolean = false,
    ): CharlesPleaseExtension {
        val mockHost = mockk<Property<String>>()
        val mockPort = mockk<Property<Int>>()
        val mockBypass = mockk<Property<String>>()
        val mockPacUrl = mockk<Property<String>>()
        val mockSerial = mockk<Property<String>>()
        val mockAutoDiscover = mockk<Property<Boolean>>()

        every { mockHost.get() } returns host
        every { mockPort.get() } returns port
        every { mockBypass.get() } returns bypass
        every { mockPacUrl.get() } returns pacUrl
        every { mockSerial.get() } returns serial
        every { mockAutoDiscover.get() } returns autoDiscover

        return object : CharlesPleaseExtension() {
            override val host = mockHost
            override val port = mockPort
            override val bypass = mockBypass
            override val pacUrl = mockPacUrl
            override val serial = mockSerial
            override val autoDiscover = mockAutoDiscover
        }
    }
}

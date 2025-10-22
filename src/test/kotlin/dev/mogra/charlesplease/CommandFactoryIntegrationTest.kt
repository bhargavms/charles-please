package dev.mogra.charlesplease

import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Integration tests for CommandFactory.
 * Uses real classes with mocked configuration properties.
 */
class CommandFactoryIntegrationTest {
    @Test
    fun `should create apply proxy command with manual configuration`() {
        // Given
        val logger = mock<Logger>()
        val config =
            createTestConfig(
                host = "192.168.1.100",
                port = 8888,
                autoDiscover = false,
            )
        val factory = CommandFactory(logger, config)

        // When
        val command = factory.createApplyProxyCommand()

        // Then
        assertNotNull(command)
        assertTrue(command is ApplyProxyCommand)
    }

    @Test
    fun `should create apply proxy command with auto-discover enabled`() {
        // Given
        val logger = mock<Logger>()
        val config =
            createTestConfig(
                host = "",
                port = 8888,
                autoDiscover = true,
            )
        val factory = CommandFactory(logger, config)

        // When
        val command = factory.createApplyProxyCommand()

        // Then
        assertNotNull(command)
        assertTrue(command is ApplyProxyCommand)
    }

    @Test
    fun `should create clear proxy command`() {
        // Given
        val logger = mock<Logger>()
        val config = createTestConfig()
        val factory = CommandFactory(logger, config)

        // When
        val command = factory.createClearProxyCommand()

        // Then
        assertNotNull(command)
        assertTrue(command is ClearProxyCommand)
    }

    @Test
    fun `should create commands with device serial`() {
        // Given
        val logger = mock<Logger>()
        val config = createTestConfig(serial = "emulator-5554")
        val factory = CommandFactory(logger, config)

        // When
        val applyCommand = factory.createApplyProxyCommand()
        val clearCommand = factory.createClearProxyCommand()

        // Then
        assertNotNull(applyCommand)
        assertNotNull(clearCommand)
        assertTrue(applyCommand is ApplyProxyCommand)
        assertTrue(clearCommand is ClearProxyCommand)
    }

    private fun createTestConfig(
        host: String = "192.168.1.100",
        port: Int = 8888,
        bypass: String = "",
        pacUrl: String = "",
        serial: String = "",
        autoDiscover: Boolean = false,
    ): CharlesPleaseExtension {
        val mockHost = mock<Property<String>>()
        val mockPort = mock<Property<Int>>()
        val mockBypass = mock<Property<String>>()
        val mockPacUrl = mock<Property<String>>()
        val mockSerial = mock<Property<String>>()
        val mockAutoDiscover = mock<Property<Boolean>>()

        whenever(mockHost.get()).thenReturn(host)
        whenever(mockPort.get()).thenReturn(port)
        whenever(mockBypass.get()).thenReturn(bypass)
        whenever(mockPacUrl.get()).thenReturn(pacUrl)
        whenever(mockSerial.get()).thenReturn(serial)
        whenever(mockAutoDiscover.get()).thenReturn(autoDiscover)

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

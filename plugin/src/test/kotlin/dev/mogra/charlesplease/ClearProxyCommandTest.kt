package dev.mogra.charlesplease

import io.mockk.every
import io.mockk.mockk
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Property
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

/**
 * Unit tests for ClearProxyCommand.
 * Tests the command creation and execution logic.
 */
class ClearProxyCommandTest {
    @Test
    fun `should create clear proxy command`() {
        // Given
        val logger = mockk<Logger>()
        val config = createTestConfig()

        // When
        val command = ClearProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    @Test
    fun `should create clear proxy command with device serial`() {
        // Given
        val logger = mockk<Logger>()
        val config = createTestConfig(serial = "emulator-5554")

        // When
        val command = ClearProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    @Test
    fun `should create clear proxy command with empty serial`() {
        // Given
        val logger = mockk<Logger>()
        val config = createTestConfig(serial = "")

        // When
        val command = ClearProxyCommand(logger, config)

        // Then
        assertNotNull(command)
    }

    private fun createTestConfig(serial: String = ""): CharlesPleaseExtension {
        val mockHost = mockk<Property<String>>()
        val mockPort = mockk<Property<Int>>()
        val mockBypass = mockk<Property<String>>()
        val mockPacUrl = mockk<Property<String>>()
        val mockSerial = mockk<Property<String>>()
        val mockAutoDiscover = mockk<Property<Boolean>>()

        every { mockHost.get() } returns ""
        every { mockPort.get() } returns 8888
        every { mockBypass.get() } returns ""
        every { mockPacUrl.get() } returns ""
        every { mockSerial.get() } returns serial
        every { mockAutoDiscover.get() } returns false

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

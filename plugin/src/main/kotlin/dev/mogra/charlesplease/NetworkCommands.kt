package dev.mogra.charlesplease

import org.gradle.api.logging.Logger
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket

/**
 * Network command classes following the command pattern.
 */
internal class DiscoverLocalIpCommand(
    private val logger: Logger,
) : Command {
    private var discoveredIp: String? = null

    override fun execute() {
        logger.info("Discovering local IP address...")
        discoveredIp = findNetworkInterfaceIp()

        if (discoveredIp.isNullOrEmpty()) {
            throw RuntimeException("Could not determine local IP address. Please specify host and port manually.")
        }

        logger.info("Found local IP: $discoveredIp")
    }

    fun getDiscoveredIp(): String = discoveredIp ?: throw IllegalStateException("Command not executed yet")

    private fun findNetworkInterfaceIp(): String? {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()

        for (networkInterface in networkInterfaces) {
            if (networkInterface.isLoopback || !networkInterface.isUp) {
                continue
            }

            val addresses = networkInterface.inetAddresses
            for (address in addresses) {
                if (address.isLoopbackAddress || address.isLinkLocalAddress) {
                    continue
                }

                val hostAddress = address.hostAddress
                if (hostAddress != null && !hostAddress.startsWith("127.") && !hostAddress.startsWith("169.254")) {
                    return hostAddress
                }
            }
        }

        return null
    }
}

/**
 * Command to test proxy connection.
 */
internal class TestProxyConnectionCommand(
    private val logger: Logger,
    private val host: String,
    private val port: Int,
) : Command {
    private var isConnected: Boolean = false

    override fun execute() {
        logger.info("Testing Charles proxy connection at $host:$port")
        isConnected = testConnection()

        if (!isConnected) {
            logger.debug("Charles proxy not detected at $host:$port")
        } else {
            logger.info("Charles proxy detected at $host:$port")
        }
    }

    fun isProxyConnected(): Boolean = isConnected

    private fun testConnection(): Boolean {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(InetAddress.getByName(host), port), 2000)
                true
            }
        } catch (e: Exception) {
            logger.debug("Connection test failed: ${e.message}")
            false
        }
    }
}

/**
 * Command to discover Charles proxy settings.
 */
internal class DiscoverCharlesProxyCommand(
    private val logger: Logger,
) : Command {
    private var discoveredHost: String? = null
    private var discoveredPort: Int? = null

    override fun execute() {
        logger.info("Discovering Charles proxy...")

        val discoverIpCommand = DiscoverLocalIpCommand(logger)
        discoverIpCommand.execute()
        val localIp = discoverIpCommand.getDiscoveredIp()

        val testConnectionCommand = TestProxyConnectionCommand(logger, localIp, 8888)
        testConnectionCommand.execute()

        if (testConnectionCommand.isProxyConnected()) {
            discoveredHost = localIp
            discoveredPort = 8888
            logger.info("Charles proxy discovered at $discoveredHost:$discoveredPort")
        } else {
            throw RuntimeException(
                "Charles proxy not detected at $localIp:8888. Make sure Charles is running and accessible.",
            )
        }
    }

    fun getDiscoveredHost(): String = discoveredHost ?: throw IllegalStateException("Command not executed yet")

    fun getDiscoveredPort(): Int = discoveredPort ?: throw IllegalStateException("Command not executed yet")
}

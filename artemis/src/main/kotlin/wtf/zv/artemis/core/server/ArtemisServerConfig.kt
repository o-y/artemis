package wtf.zv.artemis.core.server

/** Config for the [io.ktor.server]. */
data class ArtemisServerConfig (
    val port: Int = 8080,
    val host: String = "0.0.0.0",
    val baseOverridePathSuffix: String = ""
)

/** Convince method, creates an instance of [ArtemisServerConfig] with the [port] set. */
fun withPort(port: Int) = ArtemisServerConfig(port = port)
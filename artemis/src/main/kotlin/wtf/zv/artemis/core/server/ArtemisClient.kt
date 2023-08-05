package wtf.zv.artemis.core.server

import wtf.zv.artemis.core.config.ArtemisConfig
import wtf.zv.artemis.core.server.internal.ArtemisServerCore

/** Artemis server entrypoint. */
class ArtemisClient {
    /**
     * Creates and starts a web server with the given [artemisConfig].
     *
     * NOTE: This is a blocking call, executing in the current thread.
     */
    fun createClient(
        artemisConfig: ArtemisConfig,
    ) = ArtemisServerCore(artemisConfig).startServer()
}
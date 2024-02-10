package wtf.zv.artemis.core.server

import wtf.zv.artemis.core.config.ArtemisConfig
import wtf.zv.artemis.core.server.internal.ArtemisServerCore

/** Artemis server entrypoint. */
object ArtemisClient {
    /**
     * Creates and starts a web server with the given [artemisConfig].
     *
     * NOTE: This is a blocking call, executing on the current thread.
     */
    fun createWithConfig(artemisConfig: ArtemisConfig) = ArtemisServerCore(artemisConfig).startServer()
}
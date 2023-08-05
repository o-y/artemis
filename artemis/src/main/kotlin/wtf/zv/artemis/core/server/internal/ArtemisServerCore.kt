package wtf.zv.artemis.core.server.internal

import wtf.zv.artemis.core.web.page.render.pagePluginsModule
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import wtf.zv.artemis.core.config.ArtemisConfig

internal class ArtemisServerCore(private val artemisConfig: ArtemisConfig) {
    fun startServer() {
        embeddedServer(
            Netty,
            host = artemisConfig.serverHost,
            port = artemisConfig.serverPort,
            module = {
                // serve page plugins
                pagePluginsModule(artemisConfig.pagePlugins.toSet())

                // serve static internal resources
                staticScriptRoute()
            }
        ).start(wait = true)
    }
}


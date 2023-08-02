package wtf.zv.artemis.core.server

import wtf.zv.artemis.core.render.pagePluginsModule
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import wtf.zv.artemis.core.page.PagePlugin
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

class ArtemisServer
internal constructor(
    private val artemisServerConfig: ArtemisServerConfig
) {
    private var pagePluginsSet: Set<PagePlugin> = setOf();

    fun bindPagePlugins(plugins: Set<KClass<out PagePlugin>>): ArtemisServer {
        assert(pagePluginsSet.isEmpty()) {
            "Encountered multiple calls to #bindPagePlugins"
        }

        pagePluginsSet = plugins.map { it.createInstance() }.toSet()

        return this
    }

    fun startServer() {
        embeddedServer(
            Netty,
            port = artemisServerConfig.port,
            module = {
                pagePluginsModule(pagePluginsSet)
            }
        ).start(wait = true)
    }
}


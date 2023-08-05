package wtf.zv.artemis.core.server.internal

import wtf.zv.artemis.core.web.page.render.pagePluginsModule
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.server.ArtemisServerConfig
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

internal class ArtemisServerCore(private val artemisServerConfig: ArtemisServerConfig) {
    private var pagePluginsSet: Set<PagePlugin> = setOf();

    fun bindPagePlugins(plugins: Set<KClass<out PagePlugin>>): ArtemisServerCore {
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
                staticScriptRoute()
                pagePluginsModule(pagePluginsSet)
            }
        ).start(wait = true)
    }
}


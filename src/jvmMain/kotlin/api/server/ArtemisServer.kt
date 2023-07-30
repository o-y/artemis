package api.server

import api.css.minifyStyleSheetWhiteSpace
import api.page.PagePlugin
import api.render.pagePluginsModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.html.body
import kotlinx.html.unsafe
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


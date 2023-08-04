package wtf.zv.artemis.core.server

import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.server.internal.ArtemisServer
import kotlin.reflect.KClass

class ArtemisRunner {
    fun runServer(
        artemisServerConfig: ArtemisServerConfig,
        vararg pagePlugins: KClass<out PagePlugin>
    ) {
        ArtemisServer(artemisServerConfig)
            .bindPagePlugins(pagePlugins.toSet())
            .startServer()
    }
}
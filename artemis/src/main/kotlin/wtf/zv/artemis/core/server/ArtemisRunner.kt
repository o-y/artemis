package wtf.zv.artemis.core.server

import wtf.zv.artemis.core.page.PagePlugin
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
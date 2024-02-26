package wtf.zv.artemis.core.server

import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.CoroutineVerticle
import wtf.zv.artemis.core.config.ArtemisConfig
import wtf.zv.artemis.core.server.http.ArtemisWebSocketHandler
import wtf.zv.artemis.core.server.http.installStaticScriptRouteModule
import wtf.zv.artemis.core.web.page.render.installPagePluginsModule

class ArtemisVerticle internal constructor(
    private val artemisConfig: ArtemisConfig
) : CoroutineVerticle() {
    override suspend fun start() {
        val router = withRouter {
            installPagePluginsModule(artemisConfig.pagePlugins)
            installStaticScriptRouteModule()
        }

        vertx.createHttpServer()
            .requestHandler(router)
            .webSocketHandler(ArtemisWebSocketHandler(artemisConfig, vertx))

            .listen(
                /* port = */ artemisConfig.serverPort,
                /* host = */ artemisConfig.serverHost
            ) { result ->
                if (result.succeeded()) {
                    println("[Artemis @core]: Artemis HTTP server running at: ${artemisConfig.serverHost}:${artemisConfig.serverPort}")
                } else {
                    println("[Artemis @core]: Failed to run start HTTP server: ${result.cause().message}")
                    throw result.cause()
                }
            }
    }

    private inline fun withRouter(crossinline block: Router.() -> Unit = {}) = Router.router(vertx).apply { block() }
}
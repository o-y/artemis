package wtf.zv.demo

import io.vertx.kotlin.coroutines.CoroutineVerticle
import wtf.zv.artemis.core.config.ArtemisConfig.Dsl.artemisServerConfig
import wtf.zv.artemis.core.server.ArtemisVerticleFactory.createVerticleWithConfig
import wtf.zv.demo.pages.HomePagePlugin
import wtf.zv.demo.pages.blog.ReadingListPagePlugin
import java.lang.RuntimeException

/**
 * TODOs:
 *  - Generalise the backend of Artemis so that an interface defines a set of tasks which can be implemented by Runners
 *    (ServerRunner, FileBuildRunner). This will allow clients to either target building static content to artemis_dist
 *    OR support clients running Kotlin code on the server before returning HTML.
 */
class DemoEntryPoint : CoroutineVerticle() {
    override suspend fun start() {
        val artemisVerticle = createVerticleWithConfig(artemisServerConfig {
            serverPort(port = 4665)

            setHotReloadWatchPaths(setOf("build/classes", "src"))

            installPagePlugin(HomePagePlugin())
            installPagePlugin(ReadingListPagePlugin())
        })

        vertx.deployVerticle(artemisVerticle)
            .onFailure {
                println("[Artemis @core]: Failed to deploy ArtemisVerticle: ${it.message}")
                throw it
            }
            .onSuccess { uuid ->
                println("[Artemis @core]: Deployed ArtemisVerticle UUID: $uuid")
            }
    }
}
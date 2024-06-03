package wtf.zv.artemis.demo

import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.html.*
import wtf.zv.artemis.common.ArtemisBaseElementIdentifier
import wtf.zv.artemis.common.toFormattedString
import wtf.zv.artemis.core.config.ArtemisConfig.Dsl.artemisServerConfig
import wtf.zv.artemis.core.server.ArtemisVerticleFactory
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.api.createBody
import wtf.zv.artemis.core.web.page.api.css.createPageStyleSheet
import wtf.zv.artemis.core.web.page.api.ofPath

/** Sample demo app, used to verify functionality. */
class EmbeddedDemo : CoroutineVerticle() {
    override suspend fun start() {
        val artemisVerticle = ArtemisVerticleFactory.createVerticleWithConfig(artemisServerConfig {
            serverPort(port = 4665)
            setDevelopmentMode(true)

            installPagePlugin(EmbeddedDemoPagePlugin())
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

private class EmbeddedDemoPagePlugin : PagePlugin() {
    override fun providePath() = ofPath("/")

    override fun provideBody() = createBody {
        div {
            pre {
                h1 {
                    + "This is a h1 heading element!"
                }

                hr()

                h2 {
                    + "This is a h2 element!"
                }
            }
        }
    }

    override fun provideStyleSheet() = createPageStyleSheet {

    }
}

enum class EmbeddedDemoIdentifier : ArtemisBaseElementIdentifier {
    DEMO_DIV_ID;

    override fun toString() = toFormattedString()
}
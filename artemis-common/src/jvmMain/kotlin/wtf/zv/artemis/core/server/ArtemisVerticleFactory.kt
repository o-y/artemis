package wtf.zv.artemis.core.server

import io.vertx.core.Verticle
import wtf.zv.artemis.core.config.ArtemisConfig
import wtf.zv.artemis.core.config.ArtemisConfigAccessor

/** Creates and returns a Vertx [Verticle] with the given [ArtemisConfig]. */
object ArtemisVerticleFactory {
    fun createVerticleWithConfig(artemisConfig: ArtemisConfig): ArtemisVerticle {
        ArtemisConfigAccessor.setConfig(artemisConfig)
        return ArtemisVerticle(artemisConfig)
    }
}
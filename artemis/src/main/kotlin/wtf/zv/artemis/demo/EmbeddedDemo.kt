package wtf.zv.artemis.demo

import wtf.zv.artemis.core.config.ArtemisConfig.Dsl.artemisServerConfig
import wtf.zv.artemis.core.server.ArtemisVerticleFactory

/** Sample demo app, used to verify functionality. */
class EmbeddedDemo {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ArtemisVerticleFactory.createVerticleWithConfig(artemisServerConfig {
                serverPort(port = 4665)
            })
        }
    }
}
package wtf.zv.artemis.demo

import kotlinx.css.i
import wtf.zv.artemis.core.config.ArtemisConfig.Dsl.artemisServerConfig
import wtf.zv.artemis.core.server.ArtemisClient

/** Sample demo app, used to verify functionality. */
fun main() {
    ArtemisClient.createWithConfig(artemisServerConfig {
        serverPort(port = 4665)
    })
}
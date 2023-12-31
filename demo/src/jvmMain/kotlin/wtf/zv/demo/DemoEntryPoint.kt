package wtf.zv.demo

import wtf.zv.artemis.core.config.ArtemisConfig.Dsl.artemisServerConfig
import wtf.zv.artemis.core.server.ArtemisClient
import wtf.zv.demo.pages.HomePagePlugin
import wtf.zv.demo.pages.JavaScriptPagePlugin

/**
 * TODOs:
 *  - Generalise the backend of Artemis so that an interface defines a set of tasks which can be implemented by Runners
 *    (ServerRunner, FileBuildRunner). This will allow clients to either target building static content to artemis_dist
 *    OR support clients running Kotlin code on the server before returning HTML.
 */
class DemoEntryPoint {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ArtemisClient.createWithConfig(artemisServerConfig {
                serverPort(port = 4665)
                installPagePlugin(HomePagePlugin::class)
                installPagePlugin(JavaScriptPagePlugin::class)
            })
        }
    }
}
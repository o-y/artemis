package demo

import api.server.ArtemisRunner
import api.server.withPort
import demo.pages.HomePagePlugin

/**
 * TODOs:
 *  - Generalise the backend of Artemis so that an interface defines a set of tasks which can be implemented by Runners
 *    (ServerRunner, FileBuildRunner). This will allow clients to either target building static content to artemis_dist
 *    OR support clients running Kotlin code on the server before returning HTML.
 */
class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val artemisRunner = ArtemisRunner()

            // TODO: Turn this into a DSL.
            artemisRunner.runServer(
                withPort(4660),
                HomePagePlugin::class
            )
        }
    }
}
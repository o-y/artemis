package wtf.zv.artemis.core.config

import wtf.zv.artemis.core.web.page.PagePlugin

/**
 * Configuration class for the Artemis library.
 *
 * TODO: Migrate to ExplicitBackingFields when the KMP K2 compiler supports them in production. This provides a
 *       boilerplate-sans approach to exposing immutable library-wide states with a mutable DSL.
 */
class ArtemisConfig private constructor() {
    /** The port number on which the server should listen. */
    var serverPort: Int = 8080

    /** The host address on which the server should bind. */
    var serverHost: String = "0.0.0.0"

    /** A base override path prefix for all server routes.  */
    var serverBaseOverridePathPrefix: String = ""

    /** Client-provided [PagePlugin]'s hosted by the server. */
    val pagePlugins: MutableSet<PagePlugin> = mutableSetOf()

    /** Specifies filesystem paths on which the development server is hot reloaded on modifications. */
    val hotReloadWatchPaths: MutableSet<String> = mutableSetOf()

    /** Builder for [ArtemisConfig]. */
    class Builder {
        private val config = ArtemisConfig()

        /** @see [ArtemisConfig.serverPort]. */
        fun serverPort(port: Int) = apply {
            config.serverPort = port
        }

        /** @see [ArtemisConfig.serverHost]. */
        fun serverHost(host: String) = apply {
            config.serverHost = host
        }

        /** @see [ArtemisConfig.serverBaseOverridePathPrefix]. */
        fun serverBaseOverridePathPrefix(prefix: String) = apply {
            config.serverBaseOverridePathPrefix = prefix
        }

        /** @see [ArtemisConfig.pagePlugins]. */
        fun installPagePlugin(pagePlugin: PagePlugin) = apply {
            config.pagePlugins.add(pagePlugin)
        }

        /** @see [ArtemisConfig.hotReloadWatchPaths]. */
        fun setHotReloadWatchPaths(set: Set<String>) = apply {
            config.hotReloadWatchPaths += set
        }

        /** Builds the [ArtemisConfig] instance. */
        fun build(): ArtemisConfig {
            return config
        }
    }

    companion object Dsl {
        /** Provides a type-safe Kotlin DSL to build instances of [ArtemisConfig]. */
        fun artemisServerConfig(block: Builder.() -> Unit) = Builder().also(block).build()
    }
}
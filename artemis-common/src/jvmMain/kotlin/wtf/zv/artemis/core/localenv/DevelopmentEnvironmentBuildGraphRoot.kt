package wtf.zv.artemis.core.localenv

import wtf.zv.artemis.root.ArtemisBuildGraphRoot

/**
 * Provides a [ArtemisBuildGraphRoot] representing the client-side environment bootstrap function.
 */
internal object DevelopmentEnvironmentBuildGraphRoot {
    // Consistent with src/jsMain/kotlin/wza/core/localenv/DevelopmentEnvironmentBootstrap.kt
    val developmentBuildGraphRoot = ArtemisBuildGraphRoot(
        packageName = "wtf.zv.artemis.core.localenv",
        functionName = "initiateDevelopmentEnvironment"
    )
}

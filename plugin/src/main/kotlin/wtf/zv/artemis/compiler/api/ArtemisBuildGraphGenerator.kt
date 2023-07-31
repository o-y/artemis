package wtf.zv.artemis.compiler.api

/** Generates the Artemis build graph which is used to expose JsExport annotated JS-targeted Kotlin to the JVM. */
class ArtemisBuildGraphGenerator {
    fun generateBuildGraph(functionKeys: List<ArtemisFunctionKey>) {
        println("[Artemis @plugin] Generating build graph for ${functionKeys.size} methods")
    }
}
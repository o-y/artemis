package wtf.zv.artemis.compiler.api

import com.squareup.kotlinpoet.FileSpec
import org.gradle.api.Project
import wtf.zv.artemis.compiler.api.internal.*
import wtf.zv.artemis.compiler.api.internal.ArtemisFunctionKey

/** Generates the Artemis build graph which is used to expose JsExport annotated JS-targeted Kotlin to the JVM. */
internal class ArtemisBuildGraphGenerator {
    private val artemisClassGenerator = ArtemisClassGenerator()

    /** Generates and returns a Set of Artemis build graph files represented as [FileSpec]s. */
    internal fun generateBuildGraphFiles(functionKeys: List<ArtemisFunctionKey>): Set<FileSpec> {
        return mutableSetOf<FileSpec>().apply {
            add(generateBuildGraphRoot())
            addAll(generateBuildGraphFileSet(functionKeys))
        }.toSet()
    }

    private fun generateBuildGraphRoot(): FileSpec {
        return artemisClassGenerator.generateArtemisBuildGraphRootFile()
    }

    private fun generateBuildGraphFileSet(functionKeys: List<ArtemisFunctionKey>): Set<FileSpec> {
        return groupByPackage(functionKeys).map {
            artemisClassGenerator.generateSubClassedArtemisBuildGraphFile(it)
        }.toSet()
    }
}
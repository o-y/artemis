package wtf.zv.artemis.compiler.api

import com.squareup.kotlinpoet.*

/** Generates the Artemis build graph which is used to expose JsExport annotated JS-targeted Kotlin to the JVM. */
internal class ArtemisBuildGraphGenerator {
    private val artemisClassGenerator = ArtemisClassGenerator()

    fun generateBuildGraph(functionKeys: List<ArtemisFunctionKey>) {
        functionKeys.groupBy { it.rawFileName.hashCode() and it.rawPackageName.hashCode() }
            .map { (_, groupFunctionKeys) ->
                val functionKeyDef = groupFunctionKeys.first()
                GroupedArtemisFunctionKeys(
                    rawFileName = functionKeyDef.rawFileName,
                    rawPackageName = functionKeyDef.rawPackageName,
                    artemisFunctionKeys = groupFunctionKeys
                )
            }
            .map { generateArtemisBuildGraphClassForPackage(it) }
            .forEach { artemisBuildGraphFile ->
                println(artemisBuildGraphFile)
            }
    }

    private fun generateArtemisBuildGraphClassForPackage(artemisFunctionGroup: GroupedArtemisFunctionKeys): FileSpec {
        val jvmFileName = artemisFunctionGroup
            .rawFileName
            .toPascalCase(artemisBuildGraphDefinition)

        val jvmFunctionKeys = artemisFunctionGroup
            .artemisFunctionKeys
            .map(::ArtemisFunctionKeyJvmRepresentation)

        val artemisBuildGraphFile = artemisClassGenerator
            .createArtemisSkeletonClass(jvmFileName)
            .addConstants(jvmFunctionKeys)
            .toFile()

        return artemisBuildGraphFile
    }

    private companion object {
        private const val artemisBuildGraphDefinition = "ArtemisBuildGraph"
    }
}

/** Representation of [ArtemisFunctionKey] clustered by [rawPackageName] and [rawFileName]. */
internal data class GroupedArtemisFunctionKeys (
    val rawPackageName: String,
    val rawFileName: String,
    val artemisFunctionKeys: List<ArtemisFunctionKey>
)

/** Representation of [ArtemisFunctionKey] with internals represented in the format the JVM environments expect. */
internal data class ArtemisFunctionKeyJvmRepresentation (
    val artemisFunctionKey: ArtemisFunctionKey
) {
    val jvmFunctionKey = artemisFunctionKey.rawFunctionName.toSnakeCase()
}
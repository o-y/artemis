package wtf.zv.artemis.compiler.api.internal

//////////////////////////
////// OBJECT DECLARATIONS
//////////////////////////

/** Low-level class representing a JsExported function and its associated package/file name. */
internal data class ArtemisFunctionKey (
    val rawPackageName: String,
    val rawFileName: String,
    val rawFunctionName: String
)

/** Representation of [ArtemisFunctionKey] clustered by [rawPackageName] and [rawFileName]. */
internal data class GroupedArtemisFunctionKey (
    val rawPackageName: String,
    val rawFileName: String,
    val artemisFunctionKeys: List<ArtemisFunctionKey>
)

/** Representation of [ArtemisFunctionKey] with internals represented in the format the JVM environments expect. */
internal data class GeneratedArtemisFunctionKey (
    private val artemisFunctionKey: ArtemisFunctionKey
) {
    val generatedFunctionKey = artemisFunctionKey.rawFunctionName.toSnakeCase()
    val generatedHash = artemisFunctionKey.hashCode()

    val javaScriptFunctionKey = artemisFunctionKey.rawFunctionName
    val javaScriptFunctionPackage = artemisFunctionKey.rawPackageName
}

//////////////////////
////// UTILITY METHODS
//////////////////////

/** Groups a set of [ArtemisFunctionKey]'s into a set of [GroupedArtemisFunctionKey]'s. */
internal fun groupByPackage(artemisFunctionKeys: List<ArtemisFunctionKey>) = artemisFunctionKeys
    .groupBy { it.rawFileName.hashCode() and it.rawPackageName.hashCode() }
    .map { (_, groupFunctionKeys) ->
        val functionKeyDef = groupFunctionKeys.first()
        GroupedArtemisFunctionKey(
            rawFileName = functionKeyDef.rawFileName,
            rawPackageName = functionKeyDef.rawPackageName,
            artemisFunctionKeys = groupFunctionKeys
        )
    }
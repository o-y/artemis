package wtf.zv.artemis.compiler.api

/** Low-level class representing a JsExported function and its associated package/file name. */
internal data class ArtemisFunctionKey (
    val rawPackageName: String,
    val rawFileName: String,
    val rawFunctionName: String
)
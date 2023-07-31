package wtf.zv.artemis.compiler.parser

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction

/** Wrapper around the [KtFile] class, simplifying access to source code derived attributes. */
internal data class ArtemisFile(
    val fileName: String,
    val packageName: String,
    val fileImports: List<String>,
    val functionDeclarations: List<ArtemisFunction>
)

/** Wrapper around the [KtFunction] class, simplifying access to the function name and any annotationsShortNames. */
internal data class ArtemisFunction(
    val functionName: String,
    val functionBody: String,
    val annotations: List<String>,
)

/** Extracts the import list from a given [KtFile]. */
internal fun KtFile.importsList(): List<FqName> {
    return importList?.imports?.map { it.importPath?.fqName!! } ?: listOf()
}

/** Extracts any [KtFunction] declarations from a given [KtFile] regardless of visibility or contents. */
internal fun KtFile.functionDeclarations(): List<KtFunction> {
    return declarations.filterIsInstance<KtFunction>()
}

/** Extracts the package name of a given [KtFile]. */
internal fun KtFile.packageName(): String {
    return packageFqName.asString()
}

/**
 * Extracts any annotationsShortNames from a given [KtFunction] and returns a [List] of its short names.
 *
 * @see [annotationsQualifiedNames] to extract the fully qualified names.
 */
internal fun KtFunction.annotationsShortNames(): List<String> {
    return annotationEntries.mapNotNull { it.shortName?.asString() }
}

/**
 * Extracts any annotationsShortNames from a given [KtFunction] and returns a [List] of its fully qualified names.
 *
 * @see [annotationsShortNames] to extract the short names.
 */
internal fun KtFunction.annotationsQualifiedNames(): List<String> {
    return annotationEntries.mapNotNull { it.name }
}

/**
 * Extracts the type of contents from a given [KtFunction].
 *
 * This will be one of { BLOCK, CALL, EMPTY }.
 * To extract the actual function contents `KtFunction.getBodyExpression.getText` can be used.
 */
internal fun KtFunction.contents(): String {
    return bodyExpression.toString()
}

/** The short name of the @kotlin.js.JsExport annotation. */
internal const val JS_EXPORT_ANNOTATION = "JsExport"

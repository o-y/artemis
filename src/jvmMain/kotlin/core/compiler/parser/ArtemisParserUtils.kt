package core.compiler.parser

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction

data class ArtemisFile(
    public val fileName: String,
    public val fileImports: List<String>,
    public val functionDeclarations: List<ArtemisFunction>
)

data class ArtemisFunction(
    public val functionName: String,
    public val functionBody: String,
    public val annotations: List<String>,
)

internal fun KtFile.importsList(): List<FqName> {
    return importList?.imports?.map { it.importPath?.fqName!! } ?: listOf()
}

internal fun KtFile.functionDeclarations(): List<KtFunction> {
    return declarations.filterIsInstance<KtFunction>()
}

internal fun KtFunction.annotations(): List<String> {
    return annotationEntries.mapNotNull { it.shortName?.asString() }
}

internal fun KtFunction.contents(): String {
    return bodyExpression.toString()
}
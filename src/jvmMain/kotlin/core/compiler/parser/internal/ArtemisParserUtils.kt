package core.compiler.parser.internal

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction

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
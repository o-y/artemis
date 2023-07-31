package wtf.zv.artemis.compiler.api

/** Naive implementation of a String to snake case converter */
internal fun String.toSnakeCase() = replace("(?<=.)[A-Z]".toRegex(), "_$0").uppercase()

/** Naive implementation of a String to camel case converter */
internal fun String.toCamelCase(optionalPrefix: String = ""): String {
    if (optionalPrefix.isEmpty()) {
        return if (isEmpty())
            this
        else
            substring(0, 1).lowercase() + subSequence(1, length)
    }

    return optionalPrefix.toCamelCase() + toCamelCase()
}

/** Naive implementation of a String to pascal case converter */
internal fun String.toPascalCase(optionalPrefix: String = ""): String {
    if (optionalPrefix.isEmpty()) {
        return if (isEmpty())
            this
        else
            substring(0, 1).uppercase() + subSequence(1, length)
    }

    return optionalPrefix.toPascalCase() + toPascalCase()
}
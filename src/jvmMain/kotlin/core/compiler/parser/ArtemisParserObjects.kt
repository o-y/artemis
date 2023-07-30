package core.compiler.parser

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
package wtf.zv.artemis.compiler.io

import com.google.common.io.Files.getFileExtension
import wtf.zv.artemis.compiler.api.ArtemisFunctionKey
import wtf.zv.artemis.compiler.parser.ArtemisParser
import wtf.zv.artemis.compiler.parser.JS_EXPORT_ANNOTATION
import java.nio.file.Files.walk
import java.nio.file.Path

class ArtemisFileParser {
    private val artemisParser = ArtemisParser()

    fun transformFilesMatching(directory: Path) {
        directory
            .parseKotlinSourceFiles()
            .forEach { artemisFunctionKey ->
                println("[Artemis @plugin]: Parsed: ${artemisFunctionKey.rawFunctionName} of ${artemisFunctionKey.rawPackageName}")
            }
    }

    private fun Path.parseKotlinSourceFiles() = walk(this, Int.MAX_VALUE)
        .filter { it.getFileExtension() == "kt" }
        .map { artemisParser.parseFile(it) }
        .flatMap { artemisFile -> artemisFile.functionDeclarations
            .stream()
            .filter { JS_EXPORT_ANNOTATION in it.annotations }
            .map {
                ArtemisFunctionKey(
                    rawPackageName = artemisFile.packageName,
                    rawFileName = artemisFile.fileName,
                    rawFunctionName = it.functionName
                )
            }
        }

    private fun Path.getFileExtension() = getFileExtension(toString())
}

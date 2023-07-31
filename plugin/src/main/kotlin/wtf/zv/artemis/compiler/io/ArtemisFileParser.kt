package wtf.zv.artemis.compiler.io

import com.google.common.io.Files.getFileExtension
import org.gradle.internal.impldep.org.bouncycastle.asn1.iana.IANAObjectIdentifiers.directory
import wtf.zv.artemis.compiler.api.ArtemisBuildGraphGenerator
import wtf.zv.artemis.compiler.api.ArtemisFunctionKey
import wtf.zv.artemis.compiler.parser.ArtemisParser
import wtf.zv.artemis.compiler.parser.JS_EXPORT_ANNOTATION
import java.io.File
import java.nio.file.Files.walk
import java.nio.file.Path
import kotlin.streams.toList

/** Parses Kotlin source sets [Set<File>] and generates a build graph which is persisted in the build/generated dir. */
class ArtemisFileParser {
    private val artemisParser = ArtemisParser()
    private val artemisBuildGraphGenerator = ArtemisBuildGraphGenerator()

    /** Parses the given Kotlin [sourceSet] and generates a build graph from any matching Kotlin files. */
    fun parseSourceSets(sourceSet: Set<File>) {
        if (sourceSet.size > 1) {
            // TODO: Avoidable by index-keying these calls and where source set size exceeds one, append "_$index" to
            //       the generated build-graph keys and hash.
            println("[Artemis @plugin]: WARNING: The number of JsMain source sets exceeds 1, if package, file and " +
                    "class names are identical, hash collisions may occur which cause client code to call methods " +
                    "from the wrong source set.")
        }

        val functionKeys = sourceSet
            .stream()
            .flatMap { it.parseKotlinSourceFiles() }
            .toList()

        artemisBuildGraphGenerator.generateBuildGraph(functionKeys)
    }

    private fun File.parseKotlinSourceFiles() = walk(this.toPath(), Int.MAX_VALUE)
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

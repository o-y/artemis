package wtf.zv.artemis.compiler.parser

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import java.nio.file.Files.readAllBytes
import java.nio.file.Path

/** Utility class which provides methods to parse and transform Kt files into [ArtemisFile] representations. */
internal class ArtemisParser {

    /** Parses a given [filePath] representing Kotlin source-code into an [ArtemisFile]. */
    fun parseFile(filePath: Path): ArtemisFile {
        val ktFile = parseKotlinFile(
            fileName = filePath.fileName.toString(),
            fileContents = filePath.openFile()
        )

        return ktFile.toArtemisFile()
    }

    /**
     * Parses a given String [fileContents] representing Kotlin source-code into an [ArtemisFile].
     *
     * As no file name is provided, the default [unnamedFileDefaultRepresentation] is delegated to.
     */
    fun parseContents(fileContents: String): ArtemisFile {
        val ktFile = parseKotlinFile(
            fileName = unnamedFileDefaultRepresentation,
            fileContents = fileContents
        )

        return ktFile.toArtemisFile()
    }

    private fun KtFile.toArtemisFile(): ArtemisFile {
        val importsList = importsList()
        val functionDeclarations = functionDeclarations()

        return ArtemisFile(
            fileName = name,
            packageName = packageName(),
            fileImports = importsList.map { it.asString() },
            functionDeclarations = functionDeclarations.map {
                ArtemisFunction(
                    functionName = it.name.orEmpty(),
                    functionBody = it.contents(),
                    annotations = it.annotationsShortNames(),
                )
            }
        )
    }

    private fun parseKotlinFile(fileName: String, fileContents: String): KtFile {
        return PsiManager
            .getInstance(kotlinCoreEnvironment)
            .findFile(
                LightVirtualFile(fileName, KotlinFileType.INSTANCE, fileContents)
            ) as KtFile
    }

    private fun Path.openFile() = String(readAllBytes(this), Charsets.UTF_8)

    private companion object {
        private val kotlinCoreEnvironment = KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration(),
            EnvironmentConfigFiles.JS_CONFIG_FILES
        ).project

        private const val unnamedFileDefaultRepresentation = "<<<UNNAMED>>>"
    }
}
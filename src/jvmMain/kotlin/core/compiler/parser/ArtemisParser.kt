package core.compiler.parser

import core.compiler.parser.internal.annotations
import core.compiler.parser.internal.contents
import core.compiler.parser.internal.functionDeclarations
import core.compiler.parser.internal.importsList
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

class ArtemisParser {
    fun parseFile(filePath: Path): ArtemisFile {
        val ktFile = parseKotlinFile(
            fileName = filePath.fileName.toString(),
            fileContents = filePath.openFile()
        )

        return ktFile.toArtemisFile()
    }

    fun parseContents(fileContents: String): ArtemisFile {
        val ktFile = parseKotlinFile(
            fileName = buildString { repeat(12) { append((0..<36).random().toString(36)) } },
            fileContents = fileContents
        )

        return ktFile.toArtemisFile()
    }

    private fun KtFile.toArtemisFile(): ArtemisFile {
        val importsList = importsList()
        val functionDeclarations = functionDeclarations()

        return ArtemisFile(
            fileName = this.name,
            fileImports = importsList.map { it.asString() },
            functionDeclarations = functionDeclarations.map {
                ArtemisFunction(
                    functionName = it.name.orEmpty(),
                    functionBody = it.contents(),
                    annotations = it.annotations(),
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

    private fun Path.openFile() = String(readAllBytes(this), Charsets.UTF_16)

    private companion object {
        private val kotlinCoreEnvironment = KotlinCoreEnvironment.createForProduction(
            Disposer.newDisposable(),
            CompilerConfiguration(),
            EnvironmentConfigFiles.JS_CONFIG_FILES
        ).project
    }
}
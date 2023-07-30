package core.compiler

import core.compiler.parser.ArtemisFile
import core.compiler.parser.ArtemisParser
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.com.intellij.psi.PsiManager
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtFunction

/**
 * Generates type safe bindings to access Kotlin function declarations targeting JavaScript from JVM-based Kotlin code.
 *
 * This exists as a separate class with its own [main] function, so that Gradle can call this during the JS compile
 * stage.
 *
 * At a high level this class performs the following:
 * - TODO
 */
class ArtemisCompiler {
    private val artemisParser = ArtemisParser()

    fun transpileFiles() {
        println("@transpileFiles")

        val fileContents = """
                package demo

                import api.ArtemisFunction
                import kotlinx.browser.window

                @ArtemisFunction
                @OptIn(ExperimentalJsExport::class)
                @JsExport
                fun sampleFunction() {
                    println("[artemis] exec: @sampleFunction: \$\window")

                    window.fetch("https://z.zv.wtf/lastfm/v1/listening")
                        .then { it.text() }
                        .then {
                            println("[artemis] lastfm response: \$\it")
                        }
                }
            """.trimIndent()

        val artemisFile: ArtemisFile = artemisParser.parseContents(fileContents)

        println("file: $artemisFile")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) = ArtemisCompiler().transpileFiles()
    }
}
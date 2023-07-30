package api.compiler

import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer
import org.jetbrains.kotlin.config.CompilerConfiguration

private val project = KotlinCoreEnvironment.createForProduction(
    Disposer.newDisposable(),
    CompilerConfiguration(),
    EnvironmentConfigFiles.JS_CONFIG_FILES
).project

fun compiler() {

}
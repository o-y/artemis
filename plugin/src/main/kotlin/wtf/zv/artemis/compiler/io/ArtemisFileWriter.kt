package wtf.zv.artemis.compiler.io

import com.squareup.kotlinpoet.FileSpec
import org.gradle.api.Project
import java.io.File

/** Writes generated Kotlin code to the Gradle project build directory. */
internal class ArtemisFileWriter {

    /** Writes the given [artemisFileSpecs]'s to the [Project.getBuildDir]. */
    fun writeToBuildDirectory(fileSpecs: Set<FileSpec>, project: Project) {
        val projectBuildDir = project.buildDir
        val projectGeneratedBuildDir = "${projectBuildDir}/${generatedBuildDirectory}"

        fileSpecs.forEach { fileSpec ->
            println("[Artemis @plugin]: NOTE: Writing to $projectGeneratedBuildDir")
            fileSpec.writeTo(projectGeneratedBuildDir.toFile())
        }
    }

    private companion object {
        // TODO: Centralised this constant which is also used by the srcDirs and buildJavaScript gradle tasks.
        private const val generatedBuildDirectory = "artemis/generated/main/kotlin"
    }
}

private fun String.toFile() = File(this)
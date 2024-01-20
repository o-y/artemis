package wtf.zv.artemis.compiler

import wtf.zv.artemis.compiler.io.ArtemisFileParser
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import wtf.zv.artemis.plugin.ArtemisPluginBase
import wtf.zv.artemis.plugin.ArtemisPluginOrchestrator
import wtf.zv.artemis.plugin.GradleBuildDefinition.ArtemisBuildJavaScript
import wtf.zv.artemis.plugin.artemisGradleGroup

/**
 * Generates type safe bindings to access Kotlin function declarations targeting JavaScript from JVM-based Kotlin code.
 *
 * At a high level this class performs the following:
 * - Defines a Gradle Plugin which executes after the `compileKotlinJsTask` has completed, which:
 * - Extracts any *.kt files, parses their contents filters by methods annotated with "@JsExport"
 * - Generates bindings for these methods and places them in the "[Project.getBuildDir]/generated" directory so that
 *   they can be used by JVM-targeting Java to reference methods defines in [jsMainSourceSet].
 */
internal class ArtemisCompilerPlugin : ArtemisPluginBase {
    private val artemisFileParser = ArtemisFileParser()

    override fun createPlugin(project: Project) {
        val artemisBuildJavaScriptBinding = project.tasks.register(artemisBuildJavaScriptBindingsTask) {
            it.group = artemisGradleGroup
            it.description = "[INTERNAL] Generates bindings for JavaScript-targeting multiplatform code"

            it.doLast {
                println("[Artemis @plugin]: NOTE: Generating JVM bindings from KMP JS targeting Kotlin code")
                transpileFiles(project)
            }
        }

        ArtemisPluginOrchestrator.registerHookFor(
            ArtemisBuildJavaScript,
            artemisBuildJavaScriptBinding.get()
        )
    }

    private fun transpileFiles(project: Project) {
        val kotlinMultiplatform = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val jsMainSourceSet = kotlinMultiplatform.sourceSets.getByName(jsMainSourceSet).kotlin.srcDirs.toSet()

        artemisFileParser.parseSourceSets(jsMainSourceSet, project)
    }

    private companion object {
        private const val artemisBuildJavaScriptBindingsTask = "artemisBuildJavaScriptBindings"
        private const val jsMainSourceSet = "jsMain"
    }
}
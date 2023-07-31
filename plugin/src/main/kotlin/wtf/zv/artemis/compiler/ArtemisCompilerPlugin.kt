package wtf.zv.artemis.compiler

import wtf.zv.artemis.compiler.io.ArtemisFileParser
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Generates type safe bindings to access Kotlin function declarations targeting JavaScript from JVM-based Kotlin code.
 *
 * At a high level this class performs the following:
 * - Defines a Gradle Plugin which executes after the [compileKotlinJsTask] has completed, which:
 * - Extracts any *.kt files, parses their contents filters by methods annotated with "@JsExport"
 * - Generates bindings for these methods and places them in the "[Project.getBuildDir]/generated" directory so that
 *   they can be used by JVM-targeting Java to reference methods defines in [jsMainSourceSet].
 */
class ArtemisCompilerPlugin : Plugin<Project> {
    private val artemisFileParser = ArtemisFileParser()

    private fun transpileFiles(project: Project) {
        val kotlinMultiplatform = project.extensions.getByType(KotlinMultiplatformExtension::class.java)
        val jsMainSourceSet = kotlinMultiplatform.sourceSets.getByName(jsMainSourceSet).kotlin.srcDirs.toSet()

        artemisFileParser.parseSourceSets(jsMainSourceSet, project)
    }

    override fun apply(project: Project) = project.afterEvaluate {
        val artemisBuildJavaScriptBindings = project.tasks.register(artemisBuildJavaScriptBindingsTask) {
            it.group = "custom"
            it.description = "Generates bindings for JavaScript-targeting multiplatform code"

            it.doLast {
                transpileFiles(project)
            }
        }

        project.tasks.named(compileKotlinJsTask).configure {
            it.dependsOn(artemisBuildJavaScriptBindings)
        }
    }

    private companion object {
        private const val artemisBuildJavaScriptBindingsTask = "artemisBuildJavaScriptBindings"
        private const val compileKotlinJsTask = "compileKotlinJs"

        private const val jsMainSourceSet = "jsMain"
    }
}
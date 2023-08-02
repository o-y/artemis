package wtf.zv.artemis.compiler

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskContainer
import wtf.zv.artemis.plugin.ArtemisPluginBase
import wtf.zv.artemis.plugin.ArtemisPluginOrchestrator
import wtf.zv.artemis.plugin.GradleBuildDefinition.ArtemisBuildJavaScript
import wtf.zv.artemis.plugin.artemisGradleGroup

/**
 * Defines a number of tasks accessible by users of the Artemis Plugin.
 *
 * Namely, these plugins setup functionality to:
 * - Creates a task to generate a JavaScript bundle from multiplatform Kotlin code targeting JavaScript.
 * - TODO: Finish off doc.
 */
internal class ArtemisTranspilerPlugin : ArtemisPluginBase {
    override fun createPlugin(project: Project) {
        project.tasks.apply {
            val updateYarn = updateYarnTask()
            val copyJavaScript = copyJavaScriptTask()

             val artemisBuildJavaScriptBundle = register(artemisBuildJavaScriptBundleTask) {
                it.group = artemisGradleGroup
                it.description = "Generates a JavaScript bundle from Kotlin code targeting JavaScript and places this in $artemisDist"

                copyJavaScript.mustRunAfter(updateYarn)
                it.dependsOn(updateYarn, copyJavaScript)

                it.doLast {
                    println("[Artemis @plugin]: JavaScript bundle built and placed in: ${project.name}/$artemisDist")
                }
            }

            ArtemisPluginOrchestrator[ArtemisBuildJavaScript] = artemisBuildJavaScriptBundle
        }
    }

    private fun TaskContainer.copyJavaScriptTask(): Task {
        val buildJavaScript = buildJavaScriptTask()

        return register(artemisInternalCopyJavaScriptTask, Copy::class.java) { it ->
            it.group = ""
            it.description = "INTERNAL TASK - Copies the generated JavaScript bundle into $artemisDist"

            it.from(buildJavaScript) {
                it.exclude("webpack.config.js")
                it.include("*.js", "*.html")
            }

            it.into(artemisDist)
        }.get()
    }

    private fun TaskContainer.buildJavaScriptTask() = named(jsBrowserProductionWebpackTask)
    private fun TaskContainer.updateYarnTask() = named(kotlinUpgradeYarnLockTask)

    private companion object {
        // TODO: Centralise this constant
        private const val artemisDist = "build/artemis/javascript"

        private const val artemisBuildJavaScriptBundleTask = "artemisBuildJavaScriptBundle"
        private const val jsBrowserProductionWebpackTask = "jsBrowserProductionWebpack"
        private const val kotlinUpgradeYarnLockTask = "kotlinUpgradeYarnLock"
        private const val artemisInternalCopyJavaScriptTask = "artemisInternalCopyJavaScript"
    }
}

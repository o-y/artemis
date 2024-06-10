package wtf.zv.artemis.compiler

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskContainer
import org.gradle.tooling.GradleConnector
import wtf.zv.artemis.plugin.ArtemisPluginBase
import wtf.zv.artemis.plugin.ArtemisPluginOrchestrator
import wtf.zv.artemis.plugin.GradleBuildDefinition.ArtemisBuildJavaScript
import wtf.zv.artemis.plugin.artemisGradleGroup
import java.io.ByteArrayOutputStream

/**
 * Defines a number of tasks accessible by users of the Artemis Plugin.
 *
 * Specifically, the [ArtemisTranspilerPlugin] defines functionality which:
 * - Generates a JavaScript bundle deriving from the js-targetting multiplatform code.
 * - Copies this bundle into build/artemis/javascript/artemis_bundle.js.
 * - Runs the kotlinUpgradeYarnLock task to make sure dependencies are up-to-date.
 */
internal class ArtemisTranspilerPlugin : ArtemisPluginBase {
    override fun createPlugin(project: Project) {
        project.tasks.apply {
            val copyJavaScript = copyJavaScriptTask()

             val artemisBuildJavaScriptBundle = register(artemisBuildJavaScriptBundleTask) {
                 it.group = artemisGradleGroup
                 it.description = "[INTERNAL] Generates a JavaScript bundle from Kotlin code targeting JavaScript and places this in $artemisDist"

                 // I can't depend on the updateYarnTask anywhere in this task whilst Gradle multi-project builds are
                 // enabled, otherwise for reasons un-fucking-known during Gradle syncs it errors because the
                 // kotlinUpgradeYarnLock task cannot be found.
                 //   - This is only an issue with Gradle multi-project builds, if the project is isolated then syncing
                 //     occurs without an issue.
                 //   - It's not a matter of the task not existing whilst multi-project builds are enabled because after
                 //     the sync, the task does exist. In-fact quite possibly during the sync it also exists.
                 //   - The task does not appear in project.tasks, nor is it loaded by another process during the sync
                 //     because a taskCreation listener does not pick up anything either.
                 //   - Possibly multi-project hides tasks with the "other" category from internal usage, however the
                 //     documentation on this topic is sparse.
                 //   - Therefore this ugly hack is used, where we create a connection with the Gradle service and
                 //     manually invoke the required task.
                 it.doFirst {
                     val outputStream = ByteArrayOutputStream()

                     println("[Artemis @plugin]: Running $kotlinUpgradeYarnLockTask as a separate process... (no output will be shown until complete)")
                     GradleConnector
                         .newConnector()
                         .forProjectDirectory(project.projectDir)
                         .connect().use { connection ->
                             connection.newBuild()
                                 .forTasks(kotlinUpgradeYarnLockTask)
                                 .setStandardOutput(outputStream)
                                 .run()
                             connection.close()
                         }

                     println("[Artemis @plugin]: Ran $kotlinUpgradeYarnLockTask, output below... ↴")
                     println(outputStream.toString(Charsets.UTF_8))
                     println("[Artemis @plugin]: Output stdout from  $kotlinUpgradeYarnLockTask... ^")
                 }

                 it.dependsOn(copyJavaScript)
                 it.mustRunAfter(kotlinUpgradeYarnLockTask)

                 it.doLast {
                    println("[Artemis @plugin]: JavaScript bundle built and placed in: ${project.name}/$artemisDist")
                }
            }

            ArtemisPluginOrchestrator.registerHookFor(
                ArtemisBuildJavaScript,
                artemisBuildJavaScriptBundle.get()
            )
        }
    }

    private fun TaskContainer.copyJavaScriptTask(): Task {
        val buildJavaScript = buildJavaScriptTask()

        return register(artemisInternalCopyJavaScriptTask, Copy::class.java) { task ->
            task.group = "other"
            task.description = "INTERNAL TASK - Copies the generated JavaScript bundle into $artemisDist"

            task.from(buildJavaScript) {
                it.exclude("webpack.config.js")
                it.include("*.js", "*.html")
            }

            task.into(artemisDist)
        }.get()
    }

    private fun TaskContainer.buildJavaScriptTask(): Task = getByName(jsBrowserProductionWebpackTask)
    // private fun TaskContainer.updateYarnTask(): Task = getByName(kotlinUpgradeYarnLockTask)

    private companion object {
        // TODO: Centralise this constant
        private const val artemisDist = "build/artemis/javascript"

        private const val artemisBuildJavaScriptBundleTask = "artemisBuildJavaScriptBundle"
        private const val jsBrowserProductionWebpackTask = "jsBrowserProductionWebpack"
        private const val kotlinUpgradeYarnLockTask = "kotlinUpgradeYarnLock"
        private const val artemisInternalCopyJavaScriptTask = "artemisInternalCopyJavaScript"
    }
}

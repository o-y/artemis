package wtf.zv.artemis.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import wtf.zv.artemis.compiler.ArtemisCompilerPlugin
import wtf.zv.artemis.compiler.ArtemisTranspilerPlugin

/**
 * Entry point to the Artemis plugin.
 *
 * This class essentially:
 *  - Creates the [ArtemisCompilerPlugin] and [ArtemisTranspilerPlugin].
 *  - Registers their existence with the [ArtemisPluginOrchestrator], which
 *  - Registers a task (ArtemisBuildJavaScript) which groups the two existing plugins
 */
class ArtemisCorePlugin : Plugin<Project> {
    private val artemisCompilerPlugin = ArtemisCompilerPlugin()
    private val artemisTranspilerPlugin = ArtemisTranspilerPlugin()

    override fun apply(project: Project) = project.afterEvaluate {
        // It is necessary to clear any registered hooks, because the artemis.plugin.* files
        // (including this one) are evaluated each time a plugin is invoked, however because
        // the ArtemisPluginOrchestrator is a singleton, its state is persisted through
        // successive runs, so each #createPlugin call registers another task, thereby leading
        // to stack overflows.
        ArtemisPluginOrchestrator.clearHooks()

        artemisCompilerPlugin.createPlugin(project)
        artemisTranspilerPlugin.createPlugin(project)

        ArtemisPluginOrchestrator.registerDefinitions(project)
    }
}
package wtf.zv.artemis.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import wtf.zv.artemis.compiler.ArtemisCompilerPlugin
import wtf.zv.artemis.compiler.ArtemisTranspilerPlugin

class ArtemisCorePlugin : Plugin<Project> {
    private val artemisCompilerPlugin = ArtemisCompilerPlugin()
    private val artemisTranspilerPlugin = ArtemisTranspilerPlugin()

    override fun apply(project: Project) = project.afterEvaluate {
        artemisCompilerPlugin.createPlugin(project)
        artemisTranspilerPlugin.createPlugin(project)

        ArtemisPluginOrchestrator.registerDefinitions(project)
    }
}
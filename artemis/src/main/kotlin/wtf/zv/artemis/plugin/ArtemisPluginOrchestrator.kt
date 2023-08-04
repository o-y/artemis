package wtf.zv.artemis.plugin

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

/** Singleton allowing plugins to register tasks in the Artemis Gradle Build Graph. */
internal object ArtemisPluginOrchestrator {
    private val registeredHooks = mutableMapOf<GradleBuildDefinition, MutableSet<Task>>()

    /** Given the Gradle [project] registered all provided tasks in the Gradle build graph. */
    internal fun registerDefinitions(project: Project) {
        // Hooking into existing Gradle task requires the task graph to be "finalised".
        registeredHooks.forEach { (definition, taskSet) ->
            if (definition.isHook) {
                project.tasks.named(definition.definitionName) {
                    it.dependsOn(taskSet)
                }
            } else {
                project.tasks.register(definition.definitionName) {
                    if (!definition.isInternal) {
                        it.group = artemisGradleGroup
                    }
                    it.description = definition.definitionDescription

                    it.doFirst {
                        println("Executing: $taskSet")
                    }

                    // for some reason it.dependsOn(taskSet) causes Stackoverflow errors
                    taskSet.forEach(it::dependsOn)
                }
            }
        }
    }

    /** Adds the given [task] to the Artemis Gradle Build Graph keyed by [def]. */
    internal operator fun set(def: GradleBuildDefinition, task: Task) =
        registerHookFor(def, task)

    /** Adds the given [taskProvider] [Task] to the Artemis Gradle Build Graph keyed by [def]. */
    internal operator fun set(def: GradleBuildDefinition, taskProvider: TaskProvider<*>) =
        registerHookFor(def, taskProvider.get())

    /** Returns the associated Artemis Gradle Build Graph for the given [def] key. */
    internal operator fun get(def: GradleBuildDefinition) =
        registeredHooks.getOrPut(def) { mutableSetOf() }

    private fun registerHookFor(def: GradleBuildDefinition, task: Task) {
        registeredHooks.getOrPut(def) { mutableSetOf() }.add(task)
    }
}

/**
 * Defines Gradle Build Graph definitions.
 *
 * @param definitionName - required name for the definition, when [isHook] is false this is displayed in the Gradle
 *        tasks list.
 *
 * @param definitionDescription - optional description for the definition, when [isHook] is false this is displayed in
 *        the Gradle tasks list.
 *
 * @param isHook - defines whether the definition is hooking into an existing Gradle task, or whether a new Gradle task
 *        should be created.
 *
 * @param isInternal - defines whether the task should be added to the Artemis Gradle Task Group - only applicable
 *        if [isHook] is false.
 */
internal enum class GradleBuildDefinition(
    val definitionName: String,
    val definitionDescription: String = "",
    val isHook: Boolean = false,
    val isInternal: Boolean = isHook,
    ) {
    ArtemisBuildJavaScript(
        definitionName = "artemisBuildJavaScript",
        definitionDescription = "Generates bindings for JS-targeting KMP code and builds the JavaScript bundle"
    )
}

/** Base interface for all Artemis Plugins. */
internal interface ArtemisPluginBase {
    fun createPlugin(project: Project)
}

/** Publicly facing Gradle Tasks Artemis group. */
const val artemisGradleGroup = "artemis"

/** Internally facing Gradle Tasks, defaults to the "other" task group. */
const val internalGradleGroup = ""
package wtf.zv.artemis.core.web.page.javascript.internal

import org.jetbrains.kotlin.utils.getSafe
import wtf.zv.artemis.core.config.ArtemisConstants.ARTEMIS_JAVASCRIPT_BUNDLE_ENTRY_POINT
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.javascript.JavaScriptKey
import wtf.zv.artemis.root.ArtemisBuildGraphRoot
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/** Processes [PagePlugin]'s annotated with [JavaScriptKey]. */
internal object JavaScriptKeyAnnotationProcessor {

    /** Extracts a [Set] of JavaScript API calls to the functions specified by the [PagePlugin]'s [JavaScriptKey]'s. */
    fun extractPluginKeys(pagePlugin: PagePlugin): Set<String> {
        val clazz = pagePlugin::class

        val javaScriptAnnotationKeys = clazz.findAnnotation<JavaScriptKey>() ?: return emptySet()
        val buildGraphRoots: Set<KClass<out ArtemisBuildGraphRoot>> = javaScriptAnnotationKeys.javascriptKeys.toSet()

        return buildGraphRoots
            .map(::extractArtemisBuildGraphRoot)
            .map(::formatToApiCall)
            .toSet()
    }

    private fun extractArtemisBuildGraphRoot(kClass: KClass<out ArtemisBuildGraphRoot>): ArtemisBuildGraphRoot {
        val declaredFields = kClass.java.declaredFields

        val functionInstanceField = declaredFields.find {
            it.type == kClass.java
        } ?: throw IllegalArgumentException("ArtemisBuildGraphRoot instance not found in class hierarchy!")

        return functionInstanceField.getSafe(null) as ArtemisBuildGraphRoot
    }

    private fun formatToApiCall(artemisBuildGraphRoot: ArtemisBuildGraphRoot): String {
        val globalContext = "window['%s']"
        val packageName = artemisBuildGraphRoot.packageName
        val functionCall = "%s();"

        return buildString {
            append(globalContext.format(ARTEMIS_JAVASCRIPT_BUNDLE_ENTRY_POINT))
            append('.')
            append(packageName)
            append('.')
            append(functionCall.format(artemisBuildGraphRoot.functionName))
            appendLine()
        }
    }
}
package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.BODY
import kotlinx.html.script
import kotlinx.html.unsafe
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.javascript.JavaScriptKey
import wtf.zv.artemis.root.ArtemisBuildGraphRoot
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

internal fun BODY.renderPageScripts(pagePlugin: PagePlugin) {
    script {
        unsafe {
            + pagePlugin.formatToJavaScript()
        }
    }
}

private fun PagePlugin.formatToJavaScript(): String {
    val buildGraphRoots = this.extractJavaScriptKeys()

    return buildString {
        buildGraphRoots.map {
            appendLine(it.formatToJavaScript())
        }
    }
}

private fun PagePlugin.extractJavaScriptKeys(): Set<ArtemisBuildGraphRoot> {
    val clazz = this::class

    val javaScriptAnnotationKeys = clazz.findAnnotation<JavaScriptKey>() ?: return emptySet()
    val buildGraphRoots: Set<KClass<out ArtemisBuildGraphRoot>> = javaScriptAnnotationKeys.javascriptKeys.toSet()

    return buildGraphRoots.map {
        extractArtemisBuildGraphRoot(it)
    }.toSet()
}


fun extractArtemisBuildGraphRoot(kClass: KClass<out ArtemisBuildGraphRoot>): ArtemisBuildGraphRoot {
    val declaredMemberProperties = kClass.java.declaredFields

    val functionInstanceField = declaredMemberProperties.find {
        it.type == kClass.java
    }

    functionInstanceField?.isAccessible = true
    return functionInstanceField?.get(null) as ArtemisBuildGraphRoot
}

private fun ArtemisBuildGraphRoot.formatToJavaScript(): String {
    return "this['ARTEMIS_ENTRYPOINT'].$this.$functionName();"
}
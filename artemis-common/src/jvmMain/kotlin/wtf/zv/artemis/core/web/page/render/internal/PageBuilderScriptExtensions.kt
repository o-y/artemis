package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.BODY
import kotlinx.html.script
import kotlinx.html.unsafe
import wtf.zv.artemis.core.config.ArtemisConfigAccessor
import wtf.zv.artemis.core.localenv.DevelopmentEnvironmentBuildGraphRoot
import wtf.zv.artemis.core.localenv.DevelopmentEnvironmentBuildGraphRoot.developmentBuildGraphRoot
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.javascript.internal.JavaScriptKeyAnnotationProcessor.formatToApiCall

/** [BODY] plugin - renders any JavaScript associated with the [pagePlugin]. */
fun BODY.renderPageScripts(pagePlugin: PagePlugin) {
    val artemisConfig = ArtemisConfigAccessor.getConfig()
    val apiCalls = pagePlugin.provideJavaScriptApiCalls().toMutableSet()

    if (artemisConfig.isDevelopmentMode) {
        apiCalls.add(formatToApiCall(developmentBuildGraphRoot))
    }

    script {
        unsafe {
            + buildString {
                apiCalls.map {
                    appendLine(it)
                }
            }
        }
    }
}
package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.BODY
import kotlinx.html.script
import kotlinx.html.unsafe
import wtf.zv.artemis.core.web.page.PagePlugin

/** [BODY] plugin - renders any JavaScript associated with the [pagePlugin]. */
fun BODY.renderPageScripts(pagePlugin: PagePlugin) {
    val apiCalls = pagePlugin.provideJavaScriptApiCalls()

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
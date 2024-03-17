package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.*
import wtf.zv.artemis.core.config.ArtemisConfigAccessor
import wtf.zv.artemis.core.config.ArtemisConstants.ARTEMIS_JAVASCRIPT_BUNDLE_URL
import wtf.zv.artemis.core.web.page.PagePlugin

/** [HTML] plugin - renders the HTML Head provided by the [pagePlugin]. */
fun HTML.renderPageHead(pagePlugin: PagePlugin) {
    val isDevelopmentMode = ArtemisConfigAccessor.getConfig().isDevelopmentMode

    head {
        if (pagePlugin.hasHead()) {
            unsafe {
                + pagePlugin.provideHead().getHtml()
            }
        }

        if (pagePlugin.hasStyle()) {
            style {
                unsafe {
                    + pagePlugin.provideStyleSheet().getStyleSheet()
                }
            }
        }

        if (pagePlugin.provideJavaScriptApiCalls().isNotEmpty() || isDevelopmentMode) {
            script {
                type = "application/javascript"
                src = ARTEMIS_JAVASCRIPT_BUNDLE_URL
            }
        }

        meta {
            name = "artemis-rendered"
        }
    }
}

/** Returns `true` if the [PagePlugin] has either a head set. */
private fun PagePlugin.hasHead() = provideHead().getHtml().isNotBlank()

/** Returns `true` if the [PagePlugin] has a stylesheet set. */
private fun PagePlugin.hasStyle() = provideStyleSheet().getStyleSheet().isNotBlank()

/** Returns `true` if the [PagePlugin] has JavaScript set. */
private fun PagePlugin.hasJavaScript() = provideJavaScriptApiCalls().isNotEmpty()
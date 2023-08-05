package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.*
import wtf.zv.artemis.core.config.ArtemisCoreConfig.ARTEMIS_JAVASCRIPT_BUNDLE_URL
import wtf.zv.artemis.core.web.page.PagePlugin

/** [HTML] plugin - renders the HTML Head provided by the [pagePlugin]. */
fun HTML.renderPageHead(pagePlugin: PagePlugin) {
    if (pagePlugin.hasHead() || pagePlugin.hasStyle() || pagePlugin.hasJavaScript()) {
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

            if (pagePlugin.provideJavaScriptApiCalls().isNotEmpty()) {
                script {
                    type = "application/javascript"
                    src = ARTEMIS_JAVASCRIPT_BUNDLE_URL
                }
            }
        }
    }
}

/** Returns `true` if the [PagePlugin] has either a head set. */
private fun PagePlugin.hasHead() = provideHead().getHtml().isNotBlank()

/** Returns `true` if the [PagePlugin] has a stylesheet set. */
private fun PagePlugin.hasStyle() = provideStyleSheet().getStyleSheet().isNotBlank()

/** Returns `true` if the [PagePlugin] has JavaScript set. */
private fun PagePlugin.hasJavaScript() = provideJavaScriptApiCalls().isNotEmpty()
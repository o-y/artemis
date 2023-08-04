package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.HTML
import kotlinx.html.head
import kotlinx.html.style
import kotlinx.html.unsafe
import wtf.zv.artemis.core.web.page.PagePlugin

internal fun HTML.renderPageHead(pagePlugin: PagePlugin) {
    if (pagePlugin.hasHead() || pagePlugin.hasStyle()) {
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
        }
    }
}

/** Returns `true` if the [PagePlugin] has either a head set. */
private fun PagePlugin.hasHead() = provideHead().getHtml().isNotBlank()

/** Returns `true` if the [PagePlugin] has a stylesheet set. */
private fun PagePlugin.hasStyle() = provideStyleSheet().getStyleSheet().isNotBlank()

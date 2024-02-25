package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import wtf.zv.artemis.core.web.page.PagePlugin

/** Renders the [PagePlugin]'s HTML code. */
internal fun PagePlugin.parseToHtmlString(): String {
    val body = provideBody().getHtml()

    return buildString {
        append("<!DOCTYPE html>\n")
        appendHTML(
            prettyPrint = true, // TODO: Make this dependent on the isDevelopmentMode variable.
            xhtmlCompatible = false
        ).html {
            renderPageHead(pagePlugin = this@parseToHtmlString)

            body {
                unsafe {
                    + body
                }

                renderPageScripts(pagePlugin = this@parseToHtmlString)
            }
        }
    }
}
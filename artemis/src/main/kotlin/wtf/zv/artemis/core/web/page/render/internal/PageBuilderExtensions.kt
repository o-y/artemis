package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import wtf.zv.artemis.core.web.page.PagePlugin

/** Renders the [PagePlugin]'s HTML code. */
internal fun PagePlugin.parseToHtmlString(): String {
    return buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html {
            renderPageHead(pagePlugin = this@parseToHtmlString)

            body {
                unsafe { + provideBody().getHtml() }
                renderPageScripts(pagePlugin = this@parseToHtmlString)
            }
        }
    }
}
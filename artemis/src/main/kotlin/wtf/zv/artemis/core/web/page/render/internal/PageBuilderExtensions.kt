package wtf.zv.artemis.core.web.page.render.internal

import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import wtf.zv.artemis.core.web.page.PagePlugin

internal fun PagePlugin.parseToHtmlString(): String {
    val htmlContents = buildString {
        append("<!DOCTYPE html>\n")
        appendHTML().html {
            renderPageHead(pagePlugin = this@parseToHtmlString)

            body {
                unsafe { + provideBody().getHtml() }
                renderPageScripts(pagePlugin = this@parseToHtmlString)
            }
        }
    }

    return htmlContents
}
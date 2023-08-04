package wtf.zv.artemis.core.web.std.html

import kotlinx.html.*

fun HTMLTag.italics(block: HTMLTag.() -> Unit = {}) {
    ITALICS(consumer).visit(block)
}

internal class ITALICS(consumer: TagConsumer<*>) :
    HTMLTag("i", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false), HtmlInlineTag {
}
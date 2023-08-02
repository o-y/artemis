import kotlinx.html.*

class ITALICS(consumer: TagConsumer<*>) :
    HTMLTag("i", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false), HtmlInlineTag {
}

fun HTMLTag.italics(block: HTMLTag.() -> Unit = {}) {
    ITALICS(consumer).visit(block)
}
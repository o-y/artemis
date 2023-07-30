package api.component

import api.render.divIdSuffix
import kotlinx.html.*
import kotlinx.html.consumers.delayed
import kotlinx.html.stream.HTMLStreamBuilder

/**
 * Creates an instance of [ComponentContents] with the given HTML contents.
 */
inline fun createComponent(
    idSuffix: String = divIdSuffix,
    crossinline block: FlowContent.() -> Unit = {},
): ComponentContents {
    val divId = buildString {
        append(idSuffix)
        append("c_") // TODO: Break this out into a constant.
        repeat(12) {
            append((0..<36).random().toString(36))
        }
    }

    // TODO: Assign div to a new ethereal component and filter that out at the render stage.
    val htmlString = buildString {
        HTMLStreamBuilder(
            out = this,
            prettyPrint = true,
            xhtmlCompatible = false
        )
            .delayed()
            .div {
                id = divId
                block()
            }
    }

    return parseContents(htmlString)
}

/**
 * Mounts the given component in any DOM tree using the [kotlinx.html] DSL.
 */
fun HTMLTag.mountComponent(component: ComponentContents) = unsafe {
    + component.getHtml()
}


/**
 * Internal usage only, clients should refer to the [createComponent] method to create instances of [ComponentContents].
 */
fun parseContents(string: String) = ComponentContents(string)
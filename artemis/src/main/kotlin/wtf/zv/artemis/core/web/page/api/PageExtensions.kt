package wtf.zv.artemis.core.web.page.api

import kotlinx.html.*
import kotlinx.html.consumers.delayed
import kotlinx.html.consumers.filter
import kotlinx.html.stream.HTMLStreamBuilder
import wtf.zv.artemis.core.config.ArtemisConstants.DIV_ID_SUFFIX

/**
 * Creates an instance of [PageContents] with the given HTML contents.
 *
 * The [createBody] utility provides a typesafe DSL to build HTML, within this utility any element deriving from
 * `kotlinx.html.*` can be used. For example:
 *
 *    import kotlinx.html.*
 *
 *    createBody {
 *       div {
 *          h1 {
 *             + "Welcome to my website!"
 *          }
 *
 *          footer {
 *             + "Copyright 2023"
 *          }
 *       }
 *    }
 *
 * NOTE: For compatability reasons all page content is wrapped in a top-level div ([kotlinx.html.div]). At the client
 * side level these can be differentiated due to their "artemis_" suffix, however this can be modified with the
 * [divIdSuffix] optional parameter.
 */
inline fun createBody(
    idSuffix: String = DIV_ID_SUFFIX,
    crossinline block: FlowContent.() -> Unit = {},
): PageContents {
    val divId = buildString {
        append(idSuffix)
        repeat(12) {
            append((0 until 36).random().toString(36))
        }
    }

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
 * Creates an instance of [PageContents] with the HTML head contents.
 *
 * This class filters for the "head" tag and skips it, this is because the style sheet
 * ([api.page.PagePlugin.provideStyleSheet]) and the head ([api.page.PagePlugin.provideHead]) are done as two separate
 * tasks. At render time when the entire HTML document is built, this will result in two heads, one with the content
 * provided by "#provideHead" (e.g titles, metadata, script tags) and the other with the pages style sheet.
 *
 * The workaround is to filter out just the "head" tag (the inner content remains untouched) and then wrap this and the
 * stylesheet in the same "head" when they're rendered.
 */
inline fun createHead(
    crossinline block : HEAD.() -> Unit = {}
): PageContents {
    val htmlString = buildString {
        HTMLStreamBuilder(
            out = this,
            prettyPrint = true,
            xhtmlCompatible = false
        )
            .filter {
                if (it.tagName == "head") SKIP else PASS
            }
            .head {
                block()
            }
    }

    return parseContents(htmlString)
}

/**
 * Internal usage only, clients should refer to the [createBody] method to create instances of [PageContents].
 */
fun parseContents(string: String) = PageContents(string)
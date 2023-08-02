package wtf.zv.artemis.core.css

import kotlinx.css.*

/**
 * Creates an inline style for a given HTML element.
 *
 * This should be used in conjunction with the HTML element "style" tag.
 *
 *    h1 {
 *       style = inlineStyle {
 *          color = Color.aquamarine
 *          fontSize = 24.px
 *       }
 *       + "Welcome to my website!"
 *    }
 */
inline fun inlineStyle(crossinline block: CssBuilder.() -> Unit = {}) =
    CssBuilder().apply(block).toString().minifyStyleSheetWhiteSpace()

/**
 * Creates a style for a given [api.page.PagePlugin].
 */
inline fun createPageStyleSheet(crossinline block: CssBuilder.() -> Unit = {}) =
    PageStyleSheet(CssBuilder().apply(block))

/**
 *
 */
fun String.minifyStyleSheetWhiteSpace() = this.replace("\n", " ").trim()

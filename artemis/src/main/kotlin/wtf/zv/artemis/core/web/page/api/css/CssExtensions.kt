package wtf.zv.artemis.core.web.page.api.css

import kotlinx.css.*
import wtf.zv.artemis.core.web.page.PagePlugin

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
inline fun inlineStyle(crossinline block: CssBuilder.() -> Unit = {}): String =
    PageStyleSheet(
        cssBuilder = CssBuilder().apply(block),
        styleSheetType = StyleSheetType.INLINE,
        styleSheetDialect = StyleSheetDialect.CSS,
    ).toString()

/**
 * Creates a style for a given [PagePlugin].
 */
inline fun createPageStyleSheet(crossinline block: CssBuilder.() -> Unit = {}) =
    PageStyleSheet(
        cssBuilder = CssBuilder().apply(block),
        styleSheetType = StyleSheetType.PAGE,
        styleSheetDialect = StyleSheetDialect.SCSS,
    ).also {
//        println("---------------------------")
//        println(it.getCssBuilder().toString())
//        println("---------------------------")
//        println(it.toString())
//        println("---------------------------")
    }
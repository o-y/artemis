package wtf.zv.artemis.core.css

import kotlinx.css.CssBuilder

/**
 * Wrapper around CSS contents mapped to pages.
 *
 * Clients should not manually create instances of this class, instead [pageStyleSheet] should be called.
 */
class PageStyleSheet constructor(private val cssBuilder: CssBuilder) {
    fun getCssBuilder() = cssBuilder

    fun getStyleSheetString() = cssBuilder.toString()
}
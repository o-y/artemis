package wtf.zv.artemis.core.web.page.api.css

import kotlinx.css.CssBuilder
import wtf.zv.artemis.core.web.page.api.css.StyleSheetDialect.*
import wtf.zv.artemis.core.web.sasstranspiler.SassTranspiler.transpileSass

/**
 * Wrapper around CSS contents mapped to pages.
 *
 * Clients should not manually create instances of this class, instead [pageStyleSheet] should be called.
 */
class PageStyleSheet constructor(
    private val cssBuilder: CssBuilder,
    private val styleSheetType: StyleSheetType,
    private val styleSheetDialect: StyleSheetDialect
) {
    fun getCssBuilder() = cssBuilder

    /** Returns the formatted style sheet. */
    fun getStyleSheet() = toString()

    override fun toString(): String {
        val markup = cssBuilder.toString()

        return when (styleSheetDialect) {
            CSS -> markup
            SCSS -> transpileSass(markup)
        }
    }
}

/** Represents the context in which this style sheet is being painted. */
enum class StyleSheetType {
    /** Page-wide CSS. */
    PAGE,

    /** HTML element inlined CSS. */
    INLINE
}

/** Represents the dialect of the given style sheet. */
enum class StyleSheetDialect {
    CSS,
    SCSS
}
package api.page

/**
 * Wrapper around HTML contents.
 *
 * Clients should not manually create instances of this class, instead [createBody] should be called.
 */
open class PageContents internal constructor(private val htmlContents: String) {
    fun getHtml() = htmlContents
}
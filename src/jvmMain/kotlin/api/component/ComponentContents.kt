package api.component

import api.page.PageContents
import kotlinx.html.*
import kotlinx.html.stream.HTMLStreamBuilder

/**
 * Wrapper around modular HTML contents.
 *
 * Clients should not manually create instances of this class, instead [createComponent] should be called.
 *
 * These components can be mounted using the [mountComponent] method.
 */
class ComponentContents internal constructor(private val htmlContents: String) : PageContents(htmlContents)

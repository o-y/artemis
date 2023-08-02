package wtf.zv.artemis.core.component

import wtf.zv.artemis.core.page.PageContents

/**
 * Wrapper around modular HTML contents.
 *
 * Clients should not manually create instances of this class, instead [createComponent] should be called.
 *
 * These components can be mounted using the [mountComponent] method.
 */
class ComponentContents internal constructor(private val htmlContents: String) : PageContents(htmlContents)

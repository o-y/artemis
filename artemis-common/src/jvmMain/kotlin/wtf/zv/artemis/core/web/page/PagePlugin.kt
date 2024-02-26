package wtf.zv.artemis.core.web.page

import wtf.zv.artemis.core.web.page.api.PageContents
import wtf.zv.artemis.core.web.page.api.PagePath
import wtf.zv.artemis.core.web.page.api.createHead
import wtf.zv.artemis.core.web.page.api.css.PageStyleSheet
import wtf.zv.artemis.core.web.page.javascript.internal.JavaScriptKeyAnnotationProcessor.extractPluginKeys
import wtf.zv.artemis.core.web.page.api.css.createPageStyleSheet
import wtf.zv.artemis.core.web.page.render.cache.CacheStrategyConfig

/**
 * Base entrypoint for creating web pages.
 *
 * Each webpage should map to exactly one instance of [PagePlugin], for composability the component library can be used
 * which helps define reusable chunks of HTML.
 */
abstract class PagePlugin {
    /**
     * Defines which URL paths the current page should serve.
     *
     * This method supports dynamic parameters and can at init even be programmatically generated which will persist
     * for the duration of the server's lifetime, or if targeting the FileBuildRunner; will have the file/directory
     * named after the path.
     *
     * @throws RuntimeException at init if there are multiple pages with conflicting path names.
     * @throws AssertionError if any of the preconditions in [PagePath] fail.
     *
     * TODO: Pipe the ktor context into provideBody, this obviously has the ramification of conflicting with the cache
     *       expiry logic. Considering the context can be made optional we can use reflection to determine whether the
     *       context is being used, in which case the cache is either aggressively disabled or keyed to hashed URL
     *       parameters.
     */
    abstract fun providePath(): PagePath

    /**
     * Defines the body of the given page, this should be created using the [createBody] DSL.
     */
    abstract fun provideBody(): PageContents

    /**
     * Defines the head of the given page, this should be created using the [createHead] DSL.
     *
     * TODO: This is not a great naming convention, maybe just make this more kotlin-y, I'd prefer a composition based
     *       API, however wrangling with the kotlinx.browser DSL is annoying.
     */
    open fun provideHead(): PageContents = createHead {}

    /**
     * Defines a CSS style sheet for the given page, this should be created using the [createPageStyleSheet] DSL.
     */
    open fun provideStyleSheet(): PageStyleSheet = createPageStyleSheet {}

    /** INTERNAL API - Provides any associated JavaScript API calls formatted as a String [Set]. */
    internal fun provideJavaScriptApiCalls() = extractPluginKeys(this)
}
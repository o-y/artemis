package api.page

import api.css.PageStyleSheet
import api.css.createPageStyleSheet
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Base entrypoint for creating web page content.
 *
 * Each webpage should map to exactly one instance of [PagePlugin], for composability the component library can be used
 * which helps define reusable chunks of HTML.
 */
abstract class PagePlugin {
    /**
     * Defines the body of the given page, this should be created using the [createBody] DSL.
     */
    abstract fun provideBody(): PageContents

    /**
     * Defines which URL paths the current page should serve.
     *
     * This method supports dynamic parameters and can at init even be programmatically generated which will persist
     * for the duration of the server's lifetime, or if targeting the FileBuildRunner; will have the file/directory
     * named after the path.
     *
     * @throws RuntimeException at init if there are multiple pages with conflicting path names.
     *
     * TODO: Pipe the ktor context into provideBody, this obviously has the ramification of conflicting with the cache
     *       expiry logic. Considering the context can be made optional we can use reflection to determine whether the
     *       context is being used, in which case the cache is either aggressively disabled or keyed to hashed URL
     *       parameters.
     */
    abstract fun providePath(): PagePath

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

    /**
     * Defines how long the page should be cached.
     *
     * TODO: This is not yet implemented. The idea of this method is to provide a timeout in which the page contents is
     *       cached by artemis rather than re-executing any dynamic Kotlin code. Similarly an @BarePage annotation
     *       should be defined which allows Artemis to optimise successive page loads by indefinitely caching pages.
     *       As a bonus this will also allows clients to target their build with the FileBuildRunner.
     */
    open fun provideCacheExpiry(): Duration = 0.seconds
}
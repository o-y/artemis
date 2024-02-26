package wtf.zv.artemis.core.config

import wtf.zv.artemis.core.web.page.api.createBody

/** Shared configuration and library-wide constants. */
object ArtemisConstants {

    /**
     * Generated JavaScript bundle API entrypoint.
     *
     * The generated JavaScript bundle exposes any JS-targeted Kotlin to the global JavaScript context under the
     * variable.
     *
     * TODO: Allow clients to configure this.
     */
    const val ARTEMIS_JAVASCRIPT_BUNDLE_ENTRY_POINT = "ARTEMIS_ENTRYPOINT"

    /**
     * The default suffix for top-level HTML Div elements rendered using [createBody].
     *
     * TODO: Allow clients to configure this.
     */
    const val DIV_ID_SUFFIX = "artemis-ssr:"

    /////////////////////////////
    /// PUBLIC ARTEMIS ROUTES ///
    /////////////////////////////

    /**
     * Generated JavaScript bundle API URL.
     *
     * TODO: Allow clients to configure this.
     */
    const val ARTEMIS_JAVASCRIPT_BUNDLE_URL = "/_/artemis/build/artemis_bundle.js"
}
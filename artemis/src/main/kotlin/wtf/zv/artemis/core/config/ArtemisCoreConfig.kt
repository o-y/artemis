package wtf.zv.artemis.core.config

import wtf.zv.artemis.core.web.page.api.createBody

/** Shared configuration and library-wide constants. */
object ArtemisCoreConfig {

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
     * Generated JavaScript bundle API URL.
     *
     * TODO: Allow clients to configure this.
     */
    const val ARTEMIS_JAVASCRIPT_BUNDLE_URL = "/_/artemis/build/artemis_bundle.js"

    /** The default suffix for top-level HTML Div elements rendered using [createBody]. */
    const val DIV_ID_SUFFIX = "artemis_"
}
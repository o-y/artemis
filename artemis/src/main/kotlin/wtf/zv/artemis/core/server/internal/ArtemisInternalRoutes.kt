package wtf.zv.artemis.core.server.internal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import wtf.zv.artemis.core.config.ArtemisConstants.ARTEMIS_JAVASCRIPT_BUNDLE_URL
import java.io.File

/** Serves the generated KMP JavaScript bundle. */
internal fun Application.staticScriptRoute() {
    val artemisBundle = File(ARTEMIS_BUNDLE_PATH)

    routing {
        get(ARTEMIS_JAVASCRIPT_BUNDLE_URL) {
            if (artemisBundle.exists()) {
                call.respondFile(artemisBundle)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

/** File location of the generated JavaScript bundle. */
private const val ARTEMIS_BUNDLE_PATH = "build/artemis/javascript/artemis_bundle.js"
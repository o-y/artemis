package wtf.zv.artemis.core.server.internal

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

internal fun Application.staticScriptRoute() {
    val artemisBundle = File(ARTEMIS_BUNDLE_PATH)

    routing {
        get("/_/artemis/build/artemis_bundle.js") {
            if (artemisBundle.exists()) {
                call.respondFile(artemisBundle)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

// TODO: Pull this out into configuration
private const val ARTEMIS_BUNDLE_PATH = "build/artemis/javascript/artemis_bundle.js"
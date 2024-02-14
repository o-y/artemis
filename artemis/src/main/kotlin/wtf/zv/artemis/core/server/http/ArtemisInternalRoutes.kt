package wtf.zv.artemis.core.server.http

import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import wtf.zv.artemis.core.config.ArtemisConfig
import wtf.zv.artemis.core.config.ArtemisConstants.ARTEMIS_JAVASCRIPT_BUNDLE_URL
import wtf.zv.artemis.core.config.ArtemisConstants.ARTEMIS_WEBHOOK_JAVASCRIPT_URL
import java.io.File

/** [Router] module to serve the generated KMP JavaScript bundle. */
internal fun Router.installStaticScriptRouteModule(artemisConfig: ArtemisConfig) {
    val artemisJavaScriptBundle = File(ARTEMIS_BUNDLE_PATH)

    get(ARTEMIS_JAVASCRIPT_BUNDLE_URL).handler { ctx ->
        if (artemisJavaScriptBundle.exists()) {
            ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/javascript")
                .sendFile(artemisJavaScriptBundle.absolutePath)
        } else {
            ctx.response()
                .setStatusCode(404)
                .end("File not found")
        }
    }

    val artemisWebSocketPath = javaClass.classLoader.getResource(ARTEMIS_WEBSOCKET_PATH)!!
    val artemisWebSocketFile = File(artemisWebSocketPath.toURI())

    get(ARTEMIS_WEBHOOK_JAVASCRIPT_URL).handler { ctx ->
        if (artemisWebSocketFile.exists()) {
            ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/javascript")
                .sendFile(artemisWebSocketFile.absolutePath)
        } else {
            ctx.response()
                .setStatusCode(404)
                .end("File not found")
        }
    }
}

/** File location of the generated JavaScript bundle. */
private const val ARTEMIS_BUNDLE_PATH = "build/artemis/javascript/artemis_bundle.js"

/** File location of the generated JavaScript bundle. */
private const val ARTEMIS_WEBSOCKET_PATH = "hotreload/artemis_websocket.js"
package wtf.zv.artemis.core.localenv.websocket

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

const val SERVER_HASH_ACK = "artemis-ws:server:hash-ack"
const val CLIENT_PING = "artemis-ws:client:ping"

val host = window.location.hostname
val port = window.location.port

internal fun startWebSocketListener() {
    val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }

    MainScope().launch {
        client.ws(
            method = HttpMethod.Get,
            host = host,
            port = port.toInt(),
            path = "/"
        ) {
            send(CLIENT_PING)

            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val message = frame.readText()
                    if (message.startsWith(SERVER_HASH_ACK)) {
                        val formattedHash = message.substringAfterLast(":")
                        println("Deployment hash remains the same: $formattedHash")
                    }
                }
            }
        }
    }
}
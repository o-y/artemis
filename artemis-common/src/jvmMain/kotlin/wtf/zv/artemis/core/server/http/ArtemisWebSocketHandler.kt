package wtf.zv.artemis.core.server.http

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket
import wtf.zv.artemis.core.config.ArtemisConfig

class ArtemisWebSocketHandler(
    private val artemisConfig: ArtemisConfig,
    private val vertx: Vertx
) : Handler<ServerWebSocket> {
    override fun handle(webSocket: ServerWebSocket) {
        val deploymentHash = lazy { vertx.deploymentIDs().joinToString("~").hashCode() }

        if (!artemisConfig.isDevelopmentMode) {
            webSocket.close()
            return
        }

        webSocket.textMessageHandler { message ->
            if (message == CLIENT_PING) {
                println("[Artemis @core]: WebSocket - got a client ping: $message")
                webSocket.writeTextMessage(SERVER_HASH_ACK.format(deploymentHash.value))
            }
        }
    }
}

private const val SERVER_HASH_ACK = "artemis-ws:server:hash-ack:%s"
private const val CLIENT_PING = "artemis-ws:client:ping"
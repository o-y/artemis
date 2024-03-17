package wtf.zv.artemis.core.server.http

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.ServerWebSocket
import kotlinx.serialization.json.Json
import wtf.zv.artemis.core.config.ArtemisConfig
import wtf.zv.artemis.core.localenv.ClientPing
import wtf.zv.artemis.core.localenv.DeploymentData

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
            val clientPing = Json.decodeFromString<ClientPing>(message)
            println("[Artemis @core]: WebSocket - client connected at unix: ${clientPing.deploymentMark}")

            val deploymentData = DeploymentData(
                deploymentHash = deploymentHash.value
            )

            webSocket.writeTextMessage(Json.encodeToString(
                DeploymentData.serializer(),
                deploymentData
            ))
        }
    }
}
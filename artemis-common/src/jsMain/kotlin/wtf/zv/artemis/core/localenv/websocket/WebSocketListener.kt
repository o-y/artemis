package wtf.zv.artemis.core.localenv.websocket

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.atomicfu.atomic
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import wtf.zv.artemis.core.localenv.ClientPing
import wtf.zv.artemis.core.localenv.DeploymentData

val host = window.location.hostname
val port = window.location.port

val client = HttpClient {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingInterval = 100
    }
}

var previousDeploymentHash by atomic(-1)

internal fun startWebSocketListener() {
    MainScope().launch {
        client.reconnectingWebSocket(
            method = HttpMethod.Get,
            host = host,
            port = port.toInt(),
            path = "/",
        ) {
            runCatching {
                println("[Artemis @client]: Sending serialized client ping...")
                sendSerialized(ClientPing())

                while (true) {
                    val deploymentData = receiveDeserialized<DeploymentData>()
                    val deploymentHash = deploymentData.deploymentHash

                    println("[Artemis @client]: Received deployment hash: $deploymentHash")

                    // Initial connection, early return after setting the websocket
                    if (previousDeploymentHash == -1) {
                        previousDeploymentHash = deploymentHash
                    } else if (deploymentHash != previousDeploymentHash) {
                        // The server deployment hash has changed, meaning the files have, too
                        println("[Artemis @client]: Deployment hash changed, reloading page")
                        window.location.reload()
                    }
                }
            }.onFailure {
                println("Cancelled or failed: $it")
            }
        }
    }
}

private suspend fun HttpClient.reconnectingWebSocket(
    method: HttpMethod = HttpMethod.Get,
    host: String? = null,
    port: Int? = null,
    path: String? = null,
    request: HttpRequestBuilder.() -> Unit = {},
    reconnectDelayMillis: Long = 1000L,
    block: suspend DefaultClientWebSocketSession.() -> Unit
) {
    while (true) {
        try {
            webSocket(
                {
                    this.method = method
                    url("ws", host, port, path)
                    request()
                },
                block
            )
        } catch (e: Throwable) {
            println("WebSocket connection failed: ${e.message}")
        } finally {
            delay(reconnectDelayMillis)
        }
    }
}
package wtf.zv.artemis.core.localenv

import wtf.zv.artemis.core.localenv.websocket.startWebSocketListener

@OptIn(ExperimentalJsExport::class)
@JsExport
fun initiateDevelopmentEnvironment() {
    println("[Artemis @client]: Initiating client environment dev support [2]")
    startWebSocketListener()
}
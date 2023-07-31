package demo

import kotlinx.browser.window
import kotlinx.coroutines.*

@OptIn(ExperimentalJsExport::class)
@JsExport
fun sampleFunction() {
    println("[artemis] exec: @sampleFunction: $window")

    MainScope().launch {
        startCoroutine()
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
fun sampleFunctionTwoAlias() = sampleFunction()

suspend fun startCoroutine() = coroutineScope {
    delay(5000)

    launch {
        delay(30)
        println("[artemis] running in background")
    }

    val listeningTo = sampleApiCall()
    println(listeningTo)
}

suspend fun sampleApiCall(): String {
    return window.fetch("https://z.zv.wtf/lastfm/v1/listening")
        .then { it.text() }
        .then { "[artemis] lastfm response: $it" }
        .await()
}
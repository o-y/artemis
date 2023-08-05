@file:OptIn(ExperimentalJsExport::class)

package wtf.zv.demo

import kotlinx.browser.window
import kotlinx.coroutines.*
import org.w3c.dom.Window

@JsExport
fun sampleFunction() {
    println("[artemis] executing sampleFunction!")

    MainScope().launch {
        window.document.body?.innerHTML = fetchLastFmStatus()
    }
}

suspend fun fetchLastFmStatus(): String {
    return window.fetch("https://z.zv.wtf/lastfm/v1/listening")
        .then { it.text() }
        .then { "[artemis] lastfm response: $it" }
        .await()
}
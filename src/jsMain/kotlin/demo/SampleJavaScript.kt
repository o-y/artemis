package demo

import kotlinx.browser.window

@OptIn(ExperimentalJsExport::class)
@JsExport
fun sampleFunction() {
    println("[artemis] exec: @sampleFunction: $window")

    window.fetch("https://z.zv.wtf/lastfm/v1/listening")
        .then { it.text() }
        .then {
            println("[artemis] lastfm response: $it")
        }
}
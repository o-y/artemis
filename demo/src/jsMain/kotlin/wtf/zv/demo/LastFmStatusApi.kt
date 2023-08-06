@file:OptIn(ExperimentalJsExport::class)

package wtf.zv.demo

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.css.a
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Default.decodeFromString

@JsExport
fun getLastFmStatus() {
    val lastFmRootDiv = document.getElementById(LAST_FM_ROOT_DIV_ID)

    MainScope().launch {
        val lastFmStatus = fetchLastFmStatus()

        val lastFmDiv = document.create.p {
            + "(was listening to) "
            b {
                classes += "hover-cursor"
                onClickFunction = {
                    window.open(
                        url  = lastFmStatus.track_url,
                        target = "blank"
                    )
                }
                + lastFmStatus.track_name
            }
            + " by "
            b {
                + lastFmStatus.artist_name
            }
        }

        lastFmRootDiv!!.append(lastFmDiv)
    }
}

@Serializable
private data class LastFmResponse(
    val currently_playing: Boolean,
    val artist_name: String,
    val album_name: String,
    val track_name: String,
    val track_url: String,
    val image_url: String
)

private suspend fun fetchLastFmStatus(): LastFmResponse {
    return window.fetch("https://z.zv.wtf/lastfm/v1/listening")
        .then { it.text() }
        .then { decodeFromString<LastFmResponse>(it) }
        .await()
}

private const val LAST_FM_ROOT_DIV_ID = "LAST_FM_STATUS"
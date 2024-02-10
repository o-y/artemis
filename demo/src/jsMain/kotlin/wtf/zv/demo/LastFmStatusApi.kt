@file:OptIn(ExperimentalJsExport::class)

package wtf.zv.demo

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Default.decodeFromString
import org.w3c.dom.Document
import org.w3c.dom.Element
import wtf.zv.artemis.common.ArtemisBaseElementIdentifier
import wtf.zv.artemis.common.id
import wtf.zv.artemis.common.unaryPlus
import wtf.zv.demo.pages.HomePage
import wtf.zv.demo.pages.HomePage.HOVER_CURSOR_CLASS

@JsExport
fun getLastFmStatus() {
    val lastFmRootDiv = document.getElementById(HomePage.LAST_FM_DIV_ID)

    MainScope().launch {
        val lastFmStatus = fetchLastFmStatus()

        val lastFmDiv = document.create.p {
            + "(was) listening to "
            b {
                classes += HOVER_CURSOR_CLASS.toString()
                onClickFunction = {
                    window.open(
                        url  = lastFmStatus.trackUrl,
                        target = "blank"
                    )
                }
                + lastFmStatus.trackName
            }
            + " by "
            b {
                + lastFmStatus.artistName
            }
        }

        lastFmRootDiv.append(lastFmDiv)
    }
}

@Serializable
private data class LastFmResponse(
    @SerialName("currently_playing") val currentlyPlaying: Boolean,
    @SerialName("artist_name") val artistName: String,
    @SerialName("album_name") val albumName: String,
    @SerialName("track_name") val trackName: String,
    @SerialName("track_url") val trackUrl: String,
    @SerialName("image_url") val imageUrl: String
)

private suspend fun fetchLastFmStatus(): LastFmResponse {
    return window.fetch("https://z.zv.wtf/lastfm/v1/listening")
        .then { it.text() }
        .then { decodeFromString<LastFmResponse>(it) }
        .await()
}

// TODO: Provide this as part of the artemis-common framework.
private fun Document.getElementById(id: ArtemisBaseElementIdentifier): Element =
    // It's safe to use the non-null cast operator here because this takes a ArtemisBaseElementIdentifier
    // enum which we can assume maps to a valid ID.
    document.getElementById(+ id)!!
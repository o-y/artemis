package core.render

import core.page.PagePlugin
import core.page.createBody
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import java.time.Instant
import kotlin.time.Duration

/** The default suffix for top-level HTML Div elements created using [createBody]. */
const val divIdSuffix = "artemis_"

fun Application.pagePluginsModule(pagePlugins: Set<PagePlugin>) {
  routing {
    pagePlugins.forEach { plugin ->
      get(plugin.providePath().getPath()) {
        val htmlContents = buildString {
          append("<!DOCTYPE html>\n")
          appendHTML()
              .html {
                if (plugin.hasHead() || plugin.hasStyle()) {
                  head {
                    if (plugin.hasHead()) {
                      unsafe {
                        + plugin.provideHead().getHtml()
                      }
                    }

                    if (plugin.hasStyle()) {
                      style { unsafe { +plugin.provideStyleSheet().getStyleSheetString() } }
                    }
                  }
                }

                body {
                  unsafe { +plugin.provideBody().getHtml() }

                  script {
                    + "console.log('test!')"
                  }
                }
              }
        }

        call.respond(
            TextContent(
              htmlContents,
              ContentType.Text.Html.withCharset(Charsets.UTF_8),
              HttpStatusCode.OK
            )
        )
      }
    }
  }
}

/** Returns `true` if the [PagePlugin] has either a head set. */
private fun PagePlugin.hasHead() = provideHead().getHtml().isNotBlank()

/** Returns `true` if the [PagePlugin] has a stylesheet set. */
private fun PagePlugin.hasStyle() = provideStyleSheet().getStyleSheetString().isNotBlank()

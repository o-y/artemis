package wtf.zv.artemis.core.web.page.render

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.render.internal.parseToHtmlString

internal fun Application.pagePluginsModule(pagePlugins: Set<PagePlugin>) {
  routing {
    pagePlugins.forEach { plugin ->
      println("[Artemis @core]: NOTE: Serving ${plugin.providePath().getPath()}")
      get(plugin.providePath().getPath()) {
        call.respond(
          TextContent(
            text = plugin.parseToHtmlString(),
            contentType = ContentType.Text.Html.withCharset(Charsets.UTF_8),
            status = HttpStatusCode.OK
          )
        )
      }
    }
  }
}

package wtf.zv.artemis.core.web.page.render

import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.Router
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.render.internal.parseToHtmlString

/** [Router] module to serve [PagePlugin]'s. */
internal fun Router.installPagePluginsModule(pagePlugins: Set<PagePlugin>) {
    pagePlugins.forEach { plugin ->
        println("[Artemis @core]: NOTE: Serving ${plugin.providePath().getPath()}")

        get(plugin.providePath().getPath()).handler { ctx ->
            ctx
                .response()
                .setStatusCode(200)
                .putHeader(HttpHeaders.CONTENT_TYPE, listOf(
                    HttpHeaders.TEXT_HTML,
                    "charset=UTF-8"
                ))
                .end(plugin.parseToHtmlString())
        }
    }
}
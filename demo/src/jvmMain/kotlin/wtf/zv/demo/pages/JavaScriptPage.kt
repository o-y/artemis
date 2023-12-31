package wtf.zv.demo.pages

import kotlinx.html.pre
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.api.createBody
import wtf.zv.artemis.core.web.page.api.ofPath

class JavaScriptPagePlugin : PagePlugin() {
  override fun provideBody() = createBody {
    pre {
      + "Hello!"
    }
  }

  override fun providePath() = ofPath("/javascript")
}

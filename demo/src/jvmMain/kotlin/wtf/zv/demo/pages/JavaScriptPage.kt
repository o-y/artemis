package wtf.zv.demo.pages

import wtf.zv.artemis.core.page.PagePlugin
import wtf.zv.artemis.core.page.createBody
import wtf.zv.artemis.core.page.ofPath

class JavaScriptPagePlugin : PagePlugin() {
  override fun provideBody() = createBody {

  }

  override fun providePath() = ofPath("/javascript/")
}
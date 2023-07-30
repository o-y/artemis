package demo.pages

import core.page.PagePlugin
import core.page.createBody
import core.page.ofPath

class JavaScriptPagePlugin : PagePlugin() {
  override fun provideBody() = createBody {

  }

  override fun providePath() = ofPath("/javascript/")
}
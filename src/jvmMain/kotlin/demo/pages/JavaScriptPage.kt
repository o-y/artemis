package demo.pages

import api.css.createPageStyleSheet
import api.css.inlineStyle
import api.page.PagePlugin
import api.page.createBody
import api.page.createHead
import api.page.ofPath
import kotlinx.css.*
import kotlinx.css.properties.transform
import kotlinx.css.properties.translate
import kotlinx.html.*

class JavaScriptPagePlugin : PagePlugin() {
  override fun provideBody() = createBody {

  }

  override fun providePath() = ofPath("/javascript/")
}
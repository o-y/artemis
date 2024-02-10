package wtf.zv.demo.pages

import kotlinx.css.*
import kotlinx.css.properties.transform
import kotlinx.css.properties.translate
import kotlinx.html.*
import wtf.zv.artemis.common.unaryPlus
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.api.createBody
import wtf.zv.artemis.core.web.page.api.createHead
import wtf.zv.artemis.core.web.page.api.css.createPageStyleSheet
import wtf.zv.artemis.core.web.page.api.css.inlineStyle
import wtf.zv.artemis.core.web.page.api.ofPath
import wtf.zv.artemis.core.web.page.javascript.JavaScriptKey
import wtf.zv.demo.ArtemisBuildGraphLastFmStatusApi.GET_LAST_FM_STATUS
import wtf.zv.demo.pages.HomePage.HOVER_CURSOR_CLASS
import wtf.zv.demo.pages.HomePage.LAST_FM_DIV_ID

@JavaScriptKey(GET_LAST_FM_STATUS::class)
class HomePagePlugin : PagePlugin() {
  override fun providePath() = ofPath("/")

  override fun provideBody() = createBody {
    val divider = "--"

    val socials = setOf(
      "github.com/o-y",
      "gitlab.com/z_v",
      "keybase.io/o_y",
      "last.fm/user/zv-"
    )

    val contact = mapOf(
      "mail   " to "zv@zv.wtf",
      "matrix " to "@zv:zv.wtf"
    )

    pre {
      text(
          buildString {
            // header
            appendLine("zv")
            appendLine(divider)
            appendLine("currently: software engineer at Google")
            appendLine("           working on android web infra")
            appendLine(divider)

            // socials
            socials.forEach { appendLine(it) }
            appendLine(divider)

            // contact
            contact.forEach {
              append(it.key)
              appendLine(it.value)
            }
            appendLine(divider)

            // discord
            appendLine("zv#0001")
            appendLine("🧙🦄")
          })
    }

    IntRange(1, 3).map {
      img {
        classes = setOf("img_$it")
        src = "https://z.zv.wtf/lastfm/v1/image"
      }
    }

    pre {
      id = + LAST_FM_DIV_ID
      style = inlineStyle {
        color = hex(0xbdc3c7)
        marginTop = 80.px
      }
    }
  }

  override fun provideHead() = createHead { title { +"zv.wtf" } }

  override fun provideStyleSheet() = createPageStyleSheet {
    img {
      height = 200.px
      width = 200.px
    }

    ".img_1" {
      position = Position.relative
      zIndex = 5
      transform {
        translate(30.px, 30.px)
      }
    }

    ".img_2" {
      position = Position.absolute
      opacity = 0.5
      filter = "saturate(0.7) blur(1px)"

      transform {
        translate(200.px * -1, 0.px)
      }
    }

    ".img_3" {
      position = Position.absolute
      opacity = 0.5

      // TODO: Write a DSL for this, similar to transform.
      filter = "contrast(1) saturate(2) opacity(0.75) blur(1.5px)"

      transform {
        translate(140.px * -1, 60.px)
      }
    }

    ".$HOVER_CURSOR_CLASS" {
      cursor = Cursor.pointer
    }
  }
}

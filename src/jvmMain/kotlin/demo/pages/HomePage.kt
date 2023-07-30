package demo.pages

import core.css.createPageStyleSheet
import core.css.inlineStyle
import core.page.PagePlugin
import core.page.createBody
import core.page.createHead
import core.page.ofPath
import kotlinx.css.*
import kotlinx.css.properties.transform
import kotlinx.css.properties.translate
import kotlinx.html.*

class HomePagePlugin : PagePlugin() {
  override fun provideBody() = createBody {
    val divider = "--"

    val socials = setOf(
      "github.com/o-y",
      "gitlab.com/z_v",
      "keybase.io/o_y",
      "last.fm/user/zv-"
    )

    val contact = mapOf(
      "post   " to "zv@zv.wtf",
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

    pre { style = inlineStyle { color = hex(0xbdc3c7) } }
  }

  override fun provideHead() = createHead { title { +"zv.wtf" } }

  override fun provideStyleSheet() = createPageStyleSheet {
    img {
      height = 200.px
      width = 200.px
    }

    rule(".img_1") {
      position = Position.relative
      zIndex = 5
      transform {
        translate(30.px, 30.px)
      }
    }

    rule(".img_2") {
      position = Position.absolute
      opacity = 0.5
      filter = "saturate(0.7) blur(1px)"

      transform {
        translate(200.px * -1, 0.px)
      }
    }

    rule(".img_3") {
      position = Position.absolute
      opacity = 0.5
      filter = "contrast(1) saturate(2) opacity(0.75) blur(1.5px)" // TODO: Write a DSL for this, similar to transform.

      transform {
        translate(140.px * -1, 60.px)
      }
    }
  }

  override fun providePath() = ofPath("/")
}

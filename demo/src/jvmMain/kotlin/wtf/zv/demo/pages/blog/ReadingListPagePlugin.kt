package wtf.zv.demo.pages.blog

import kotlinx.css.*
import kotlinx.html.*
import wtf.zv.artemis.common.unaryPlus
import wtf.zv.artemis.core.web.page.PagePlugin
import wtf.zv.artemis.core.web.page.api.createBody
import wtf.zv.artemis.core.web.page.api.css.createPageStyleSheet
import wtf.zv.artemis.core.web.page.api.ofPath
import wtf.zv.demo.pages.blog.ReadingList.READING_LIST_DIV_ID

class ReadingListPagePlugin : PagePlugin() {
  override fun providePath() = ofPath("/~/reading")

  override fun provideBody() = createBody {
    div {
      id = + READING_LIST_DIV_ID

      table {
        caption {
          + "List 1 - Mystery"
        }
        tbody {
          tr {
            td { + "The Da Vinci Code" }
            td { + "Dan Brown" }
          }
          tr {
            td { + "Gone Girl" }
            td { + "Gillian Flynn" }
          }
          tr {
            td { + "Sherlock Holmes" }
            td { + "Arthur Conan Doyle" }
          }
        }
      }
    }
  }

  override fun provideStyleSheet() = createPageStyleSheet {
    "*" {
      fontFamily = "monospace"
      whiteSpace = WhiteSpace.nowrap
    }

    READING_LIST_DIV_ID.cssId {
      display = Display.flex

      table {
        borderCollapse = BorderCollapse.collapse
        borderSpacing = 50.px
        marginRight = 40.px
        backgroundColor = Color.blue

        td {
          padding = Padding(right = 20.px)
          backgroundColor = Color.red

          hover {
            backgroundColor = Color.green
          }
        }

        caption {
          fontWeight = FontWeight.bold
          textAlign = TextAlign.left
          backgroundColor = Color.yellow
        }
      }
    }
  }
}
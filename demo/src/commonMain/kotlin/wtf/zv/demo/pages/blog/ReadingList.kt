package wtf.zv.demo.pages.blog

import wtf.zv.artemis.common.ArtemisBaseElementIdentifier
import wtf.zv.artemis.common.toFormattedString

enum class ReadingList : ArtemisBaseElementIdentifier {
    READING_LIST_DIV_ID;

    override fun toString() = toFormattedString()
}
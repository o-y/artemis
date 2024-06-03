package wtf.zv.demo.pages

import wtf.zv.artemis.common.ArtemisBaseElementIdentifier
import wtf.zv.artemis.common.toFormattedString

enum class HomePage : ArtemisBaseElementIdentifier {
    /** The pre tag which displays the current Last.fm listening to data. */
    LAST_FM_DIV_ID,

    /** Utility class - maps to the album images. */
    LAST_FM_IMAGE_CLASS,

    /** Utility class - displays the hand when the cursor is hovered other attributed elements. */
    HOVER_CURSOR_CLASS;

    override fun toString() = toFormattedString()
}
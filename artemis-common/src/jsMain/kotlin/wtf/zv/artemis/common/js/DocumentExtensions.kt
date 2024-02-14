package wtf.zv.artemis.common.js

import kotlinx.browser.document
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.WebSocket
import wtf.zv.artemis.common.ArtemisBaseElementIdentifier
import wtf.zv.artemis.common.unaryPlus

/** Retrieves a [Element] given an [ArtemisBaseElementIdentifier] ID. */
fun Document.getElementById(id: ArtemisBaseElementIdentifier): Element =
    // It's safe to use the non-null cast operator here because this takes a
    // ArtemisBaseElementIdentifier enum which we can assume maps to a valid ID.
    document.getElementById(+ id)!!

/** @see [getElementById]. */
fun Document.getArtemisElement(id: ArtemisBaseElementIdentifier) = getElementById(id)
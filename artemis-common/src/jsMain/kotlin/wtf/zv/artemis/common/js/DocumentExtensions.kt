package wtf.zv.artemis.common.js

import kotlinx.browser.document
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLCollection
import wtf.zv.artemis.common.ArtemisBaseElementIdentifier
import wtf.zv.artemis.common.unaryPlus

// getElementById

/** Retrieves a [Element] given an [ArtemisBaseElementIdentifier] ID. */
fun Document.getElementById(id: ArtemisBaseElementIdentifier): Element =
    // It's safe to use the non-null cast operator here because this takes a
    // ArtemisBaseElementIdentifier enum which we can assume maps to a valid ID.
    document.getElementById(+ id)!!

/** Retrieves a sub-classed [Element] of type [T] given an [ArtemisBaseElementIdentifier] ID. */
inline fun <reified T: Element> Document.getElementById(id: ArtemisBaseElementIdentifier): T =
// It's safe to use the non-null cast operator here because this takes a
    // ArtemisBaseElementIdentifier enum which we can assume maps to a valid ID.
    document.getElementById(+ id)!! as T

// getElementsByClassName

/**
 * Retrieves a [Element] given an [ArtemisBaseElementIdentifier] classname.
 *
 * See [HTMLCollection.forEach] and [HTMLCollection.toList] for idiomatic, type-safe
 * [HTMLCollection] APIs.
 */
fun Document.getElementsByClassName(className: ArtemisBaseElementIdentifier): HTMLCollection =
    document.getElementsByClassName(+ className)
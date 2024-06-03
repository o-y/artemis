package wtf.zv.artemis.common.js

import org.w3c.dom.Element
import org.w3c.dom.HTMLCollection
import org.w3c.dom.get

/** Iterates through the given [HTMLCollection], applying [action]. */
inline fun HTMLCollection.forEach(action: (Element) -> Unit) {
    for (i in 0 until length) action(requireNotNull(this[i]) {
        "[Artemis @client] Unexpected error - element $i does not exist for HTMLCollection: $this"
    })
}

/** Iterates through the given [HTMLCollection], applying [action] after casting to type [T]. */
inline fun <reified T : Element> HTMLCollection.forEach(action: (T) -> Unit) {
    toList<T>().forEach(action)
}

/** Maps the given [HTMLCollection] to a [List] of [Element]s. */
inline fun <reified T : Element> HTMLCollection.toList(): List<T> =
    mutableListOf<T>().apply {
        this@toList.forEach { this.add(it as T) }
    }.toList()
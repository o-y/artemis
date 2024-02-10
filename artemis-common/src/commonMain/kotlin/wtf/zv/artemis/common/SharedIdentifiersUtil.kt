package wtf.zv.artemis.common

import kotlin.properties.ReadOnlyProperty
import kotlin.random.Random

inline fun <reified T> T.toFormattedString(): String where T : Enum<T>, T : ArtemisBaseElementIdentifier {
    return "artemis-js-${name.lowercase()}"
}

/** Provides run-time-wide-immutable random hex-code IDs for JS-based elements. */
// Invoke like so: idGenerator.getValue(this, ::idGenerator)
private val idGenerator = ReadOnlyProperty<ArtemisBaseElementIdentifier, String> { _, _ ->
    Random.nextInt(0x1000000).toString(16)
}

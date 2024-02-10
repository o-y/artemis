package wtf.zv.artemis.common

import kotlin.properties.ReadOnlyProperty
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

/** Defines the base interface for KMP-Common HTML class/ID attribute definitions. */
interface ArtemisBaseElementIdentifier {
    val id: String
        get() = "$this"
}

/** Returns a raw [ArtemisBaseElementIdentifier] for HTML ID attribute definitions. */
operator fun ArtemisBaseElementIdentifier.unaryPlus(): String {
    return id
}

/** Returns a raw [ArtemisBaseElementIdentifier] for HTML ID attribute definitions. */
fun ArtemisBaseElementIdentifier.id(): String {
    return id
}
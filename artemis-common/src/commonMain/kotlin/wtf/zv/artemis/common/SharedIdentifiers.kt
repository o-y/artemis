package wtf.zv.artemis.common

/** Defines the base interface for KMP-Common HTML class/ID attribute definitions. */
interface ArtemisBaseElementIdentifier {
    val ref: String
        get() = "$this"

    /** Formats the [ArtemisBaseElementIdentifier] as an ID rule for CSS definitions. */
    // TODO: Scope this to PageStyleSheet once context receivers reach opt-in status.
    val cssId: String
        get() = "#$ref"

    /** Formats the [ArtemisBaseElementIdentifier] as a class rule for CSS definitions. */
    val cssClass: String
        get() = ".$ref"
}

/** Returns a raw [ArtemisBaseElementIdentifier] for HTML attribute references. */
operator fun ArtemisBaseElementIdentifier.unaryPlus(): String {
    return ref
}
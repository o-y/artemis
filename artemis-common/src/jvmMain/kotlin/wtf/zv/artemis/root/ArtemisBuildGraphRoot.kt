package wtf.zv.artemis.root

/** Defines generated JavaScript stubs. */
public open class ArtemisBuildGraphRoot(
    public open val functionName: String,
    public open val packageName: String,
    public open val functionHash: String = (functionName + packageName).hashCode().toString(),
)

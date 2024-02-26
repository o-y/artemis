package wtf.zv.artemis.core.config

/** Provides a standard entrypoint to the Artemis Config. */
internal object ArtemisConfigAccessor {
    private var config: ArtemisConfig? = null

    fun getConfig() = config ?: throw RuntimeException("Attempted to call #getConfig before " +
                                                       "the ArtemisVerticle has been initiated")

    fun setConfig(providedConfig: ArtemisConfig) {
        if (config != null) {
            throw RuntimeException("Attempted to redeclare the ArtemisConfig after the ArtemisVerticle " +
                                   "has already been initiated")
        }

        config = providedConfig
    }
}
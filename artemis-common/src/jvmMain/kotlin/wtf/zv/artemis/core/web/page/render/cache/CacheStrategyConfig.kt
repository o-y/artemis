package wtf.zv.artemis.core.web.page.render.cache

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class CacheStrategyConfig(
    val aggressivelyPrecache: Boolean = false,
    val cacheDuration: Duration = 0.seconds
) {
    init {
        assert(cacheDuration.isPositive() && aggressivelyPrecache) {
            "#aggressivelyPrecache should not be enabled at the same time as #cacheDuration"
        }
    }
}
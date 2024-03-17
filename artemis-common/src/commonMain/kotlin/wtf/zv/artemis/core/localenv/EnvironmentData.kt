package wtf.zv.artemis.core.localenv

import kotlinx.datetime.Clock.System.now
import kotlinx.serialization.Serializable

@Serializable
data class DeploymentData(
    val deploymentHash: Int,
) {
    val deploymentMark = now().epochSeconds
}

@Serializable
class ClientPing {
    val deploymentMark = now().epochSeconds
}
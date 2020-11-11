package base_http_server.config

import io.vertx.core.DeploymentOptions

interface VerticleConfig {
    val deploymentOptions: DeploymentOptions
}

object ServerConfig : VerticleConfig {
    override val deploymentOptions: DeploymentOptions by lazy {
        DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors())
    }
}

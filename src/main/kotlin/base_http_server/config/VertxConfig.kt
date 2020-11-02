package base_http_server.config

import io.vertx.core.DeploymentOptions
import proxy.HealthService

interface ConfigNaming {
    val prefix: String
}

interface VerticleConfig {
    val deploymentOptions: DeploymentOptions
}

abstract class ServiceConfig : ConfigNaming, VerticleConfig {
    override val prefix: String = "service"
    protected abstract val eventBusTopic: String

    fun eventBusTopic(): String {
        return "$prefix/$eventBusTopic"
    }
}

object HealthServiceConfig : ServiceConfig() {
    override val deploymentOptions: DeploymentOptions by lazy {
        DeploymentOptions().setInstances(1)
    }

    override val eventBusTopic: String = HealthService::class.java.simpleName
}

object ServerConfig : VerticleConfig {
    override val deploymentOptions: DeploymentOptions by lazy {
        DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors())
    }
}

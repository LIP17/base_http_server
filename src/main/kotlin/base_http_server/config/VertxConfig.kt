package base_http_server.config

import base_http_server.service.verticles.HealthServiceVerticle
import base_http_server.service.verticles.HttpServerVerticle
import io.vertx.core.DeploymentOptions
import proxy.HealthService

interface ConfigNaming {
    val prefix: String
}

interface VerticleConfig {
    val deploymentOptions: DeploymentOptions
    val verticleCanonicalName: String
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
    override val verticleCanonicalName: String by lazy {
        HealthServiceVerticle::class.java.canonicalName
    }
}

object ServerConfig : VerticleConfig {
    override val deploymentOptions: DeploymentOptions by lazy {
        DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors())
    }
    override val verticleCanonicalName: String by lazy {
        HttpServerVerticle::class.java.canonicalName
    }
}

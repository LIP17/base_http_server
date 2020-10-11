import base_http_server.config.HealthServiceConfig
import base_http_server.config.ServerConfig
import io.vertx.reactivex.core.Vertx

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val vertx = Vertx.vertx()

        val healthServiceVerticle = vertx.rxDeployVerticle(
            HealthServiceConfig.verticleCanonicalName,
            HealthServiceConfig.deploymentOptions
        )
        val serverVerticle = vertx.rxDeployVerticle(
            ServerConfig.verticleCanonicalName,
            ServerConfig.deploymentOptions
        )

        healthServiceVerticle
            .flatMap { serverVerticle }
            .doOnSuccess { println("Deploy finished") }
            .doOnError { e -> println("Deploy failed because ${e.message}") }
            .subscribe()
    }
}

import base_http_server.config.HealthServiceConfig
import base_http_server.config.ServerConfig
import io.vertx.reactivex.core.Vertx

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val vertx = Vertx.vertx()
        vertx.rxDeployVerticle(
            HealthServiceConfig.verticleCanonicalName,
            HealthServiceConfig.deploymentOptions
        )
            .flatMap { _ ->
                vertx.rxDeployVerticle(
                    ServerConfig.verticleCanonicalName,
                    ServerConfig.deploymentOptions
                )
            }
            .doOnSuccess { println("Deploy finished") }
            .doOnError { e -> println("Deploy failed because ${e.message}") }
            .subscribe()
    }
}

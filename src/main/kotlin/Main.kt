import base_http_server.config.HealthServiceConfig
import base_http_server.config.ServerConfig
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val vertx = Vertx.vertx()

        GlobalScope.launch(vertx.dispatcher()) {
            vertx.deployVerticleAwait(
                HealthServiceConfig.verticleCanonicalName,
                HealthServiceConfig.deploymentOptions
            )

            vertx.deployVerticleAwait(
                ServerConfig.verticleCanonicalName,
                ServerConfig.deploymentOptions
            )
        }
    }
}

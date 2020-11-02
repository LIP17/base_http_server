import base_http_server.config.HealthServiceConfig
import base_http_server.config.ServerConfig
import base_http_server.module.MainModule
import base_http_server.service.verticles.HealthServiceVerticle
import base_http_server.service.verticles.HttpServerVerticle
import com.google.inject.Guice
import com.google.inject.Stage
import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val injector = Guice.createInjector(Stage.PRODUCTION, MainModule())
        val vertx = injector.getInstance(Vertx::class.java)

        GlobalScope.launch(vertx.dispatcher()) {
            vertx.deployVerticleAwait(
                { injector.getInstance(HealthServiceVerticle::class.java) },
                HealthServiceConfig.deploymentOptions
            )

            vertx.deployVerticleAwait(
                { injector.getInstance(HttpServerVerticle::class.java )},
                ServerConfig.deploymentOptions
            )
        }
    }
}

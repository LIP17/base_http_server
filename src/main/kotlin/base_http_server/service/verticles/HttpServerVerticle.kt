package base_http_server.service.verticles

import base_http_server.common.BaseVerticle
import base_http_server.config.HealthServiceConfig
import base_http_server.service.factory.HealthServiceFactory
import io.reactivex.Completable
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import proxy.reactivex.HealthService

class HttpServerVerticle : BaseVerticle() {

    private lateinit var healthService: HealthService

    override fun rxStart(): Completable {
        val server = vertx.createHttpServer()
        val router = configureRouting()

        healthService = HealthServiceFactory.createProxy(vertx.delegate, HealthServiceConfig.eventBusTopic())

        return server
            .requestHandler(router)
            .rxListen(8080)
            .ignoreElement()
    }

    private fun configureRouting(): Router {
        val router = Router.router(vertx)

        val healthRouter = Router.router(vertx)
        healthRouter.get("/").handler(::healthHandler)

        router.mountSubRouter("/health", healthRouter)
        return router
    }

    private fun healthHandler(context: RoutingContext) {
        healthService.rxIsAlive()
            .doOnSuccess {
                val response = JsonObject()
                response.put("success", it)
                context.response().statusCode = 200
                context.response().putHeader("Content-Type", "application/json")
                context.response().end(response.encode())
            }
            .doOnError { e ->
                println("Error ${e.message}")
            }
            .subscribe()
    }
}

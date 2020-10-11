package base_http_server.service.verticles

import base_http_server.common.AbstractVerticle
import base_http_server.config.HealthServiceConfig
import base_http_server.service.factory.HealthServiceFactory
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import proxy.reactivex.HealthService

class ServerVerticle : AbstractVerticle() {

    private lateinit var healthService: HealthService

    override fun start(startPromise: io.vertx.core.Promise<Void>) {
        val server = vertx.createHttpServer()
        val router = configureRouting()

        healthService = HealthServiceFactory.createProxy(vertx.delegate, HealthServiceConfig.eventBusTopic())

        server
            .requestHandler(router)
            .rxListen(8080)
            .doOnSuccess { _ ->
                startPromise.complete()
            }
            .subscribe()
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

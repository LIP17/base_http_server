package base_http_server.service.verticles

import base_http_server.config.HealthServiceConfig
import base_http_server.service.factory.HealthServiceFactory
import base_http_server.service.impl.isAliveAwait
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch
import proxy.HealthService

class HttpServerVerticle : CoroutineVerticle() {

    private lateinit var healthService: HealthService

    override suspend fun start() {
        val server = vertx.createHttpServer()
        val router = configureRouting()

        healthService = HealthServiceFactory.createProxy(vertx, HealthServiceConfig.eventBusTopic())

        server
            .requestHandler(router)
            .listenAwait(8080)
    }

    private fun configureRouting(): Router {
        val router = Router.router(vertx)

        val healthRouter = Router.router(vertx)
        healthRouter.get("/").coroutineHandler(::healthHandler)

        router.mountSubRouter("/health", healthRouter)
        return router
    }

    private suspend fun healthHandler(context: RoutingContext) {
        val isAlive = healthService.isAliveAwait()
        val response = JsonObject()
        response.put("success", isAlive)
        context.response().statusCode = 200
        context.response().putHeader("Content-Type", "application/json")
        context.response().end(response.encode())
    }

    /**
     * An extension method for simplifying coroutines usage with Vert.x Web routers
     */
    private fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
        handler { ctx ->
            launch(ctx.vertx().dispatcher()) {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                }
            }
        }
    }
}

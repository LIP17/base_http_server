import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import java.util.UUID

class ServerVerticle: AbstractVerticle() {

    private val VERTICLE_ID = UUID.randomUUID().toString()

    override fun start(startPromise: io.vertx.core.Promise<Void>) {
        val server = vertx.createHttpServer()
        val router = configureRouting()

        server
            .requestHandler(router)
            .rxListen(8080).subscribe{ _ -> println("$VERTICLE_ID started in deployment ${deploymentID()}") }
    }

    private fun configureRouting(): Router {
        val router = Router.router(vertx)

        val healthRouter = Router.router(vertx)
        healthRouter.get("/").handler { context ->
            val response = JsonObject()
            response.put("success", true)
            context.response().statusCode = 200
            context.response().putHeader("Content-Type", "application/json")
            context.response().end(response.encode())
        }

        router.mountSubRouter("/health", healthRouter)
        return router
    }

}
package base_http_server.module

import base_http_server.controllers.HealthController
import base_http_server.service.verticles.coroutineHandler
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class VertxModule: AbstractModule() {

    @Provides
    @Singleton
    fun getVertx(): Vertx {
        return Vertx.vertx()
    }

    @Provides
    @Singleton
    fun routerConfiguration(vertx: Vertx, @Named("health-service-router") healthRouter: Router): Router {
        val router = Router.router(vertx)
        router.mountSubRouter("/health", healthRouter)
        return router
    }


    @Provides
    @Singleton
    @Named("health-service-router")
    fun getHealthRouter(vertx: Vertx, controller: HealthController): Router {
        val healthRouter = Router.router(vertx)
        healthRouter.get("/").coroutineHandler(controller::healthHandler)
        return healthRouter
    }
}
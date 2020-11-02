package base_http_server.module

import base_http_server.config.HealthServiceConfig
import base_http_server.controllers.HealthController
import base_http_server.service.impl.HealthServiceImpl
import base_http_server.service.verticles.coroutineHandler
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import proxy.HealthService
import proxy.HealthServiceVertxEBProxy

class HealthServiceModule: AbstractModule() {

    @Provides
    @Singleton
    @Named("health-service-impl")
    fun getHealthServiceImpl(): HealthService {
        return HealthServiceImpl()
    }

    @Provides
    @Singleton
    @Named("health-service-proxy")
    fun getHealthServiceProxy(vertx: Vertx): HealthService {
        return HealthServiceVertxEBProxy(vertx, HealthServiceConfig.eventBusTopic())
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
package base_http_server.module

import base_http_server.service.verticles.HealthServiceVerticle
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import proxy.HealthService

class VertxModule: AbstractModule() {

    @Provides
    @Singleton
    fun getVertx(): Vertx {
        return Vertx.vertx()
    }

    @Provides
    fun getHealthServiceVerticle(@Named("health-service-impl") healthServiceImpl: HealthService): HealthServiceVerticle {
        return HealthServiceVerticle(healthServiceImpl)
    }

    @Provides
    @Singleton
    fun routerConfiguration(vertx: Vertx, @Named("health-service-router") healthRouter: Router): Router {
        val router = Router.router(vertx)
        router.mountSubRouter("/health", healthRouter)
        return router
    }
}
package base_http_server.service.verticles

import base_http_server.config.HealthServiceConfig
import base_http_server.service.factory.HealthServiceFactory
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.serviceproxy.ServiceBinder
import proxy.HealthService

class HealthServiceVerticle : CoroutineVerticle() {

    override suspend fun start() {
        val healthServiceImpl = HealthServiceFactory.createImpl()
        val binder = ServiceBinder(vertx)

        binder.setAddress(HealthServiceConfig.eventBusTopic())
            .register(HealthService::class.java, healthServiceImpl)
    }
}

package base_http_server.service.verticles

import base_http_server.config.HealthServiceConfig
import base_http_server.service.factory.HealthServiceFactory
import io.vertx.core.Promise
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.serviceproxy.ServiceBinder
import proxy.HealthService

class HealthServiceVerticle: AbstractVerticle() {

    override fun start(startPromise: Promise<Void>) {
        val healthServiceImpl = HealthServiceFactory.createImpl()
        val binder = ServiceBinder(vertx.delegate)
        binder.setAddress(HealthServiceConfig.eventBusTopic()).register(HealthService::class.java, healthServiceImpl)
            .completionHandler {
                startPromise.complete()
            }
    }
}

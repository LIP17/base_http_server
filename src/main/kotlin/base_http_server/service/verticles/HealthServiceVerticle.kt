package base_http_server.service.verticles

import base_http_server.common.BaseVerticle
import base_http_server.config.HealthServiceConfig
import base_http_server.service.factory.HealthServiceFactory
import io.reactivex.Completable
import io.vertx.serviceproxy.ServiceBinder
import proxy.HealthService

class HealthServiceVerticle : BaseVerticle() {

    override val startUpTimeoutInSeconds: Long = 1L

    override fun rxStart(): Completable {
        val healthServiceImpl = HealthServiceFactory.createImpl()
        val binder = ServiceBinder(vertx.delegate)

        return Completable.create { emitter ->
            binder.setAddress(HealthServiceConfig.eventBusTopic())
                .register(HealthService::class.java, healthServiceImpl)
                .completionHandler {
                    if (it.succeeded()) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(it.cause())
                    }
                }
        }
    }
}

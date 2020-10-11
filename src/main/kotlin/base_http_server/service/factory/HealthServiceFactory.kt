package base_http_server.service.factory

import base_http_server.service.impl.HealthServiceImpl
import io.vertx.core.Vertx
import proxy.HealthService
import proxy.HealthServiceVertxEBProxy

object HealthServiceFactory {

    fun createImpl(): HealthService {
        return HealthServiceImpl()
    }

    fun createProxy(vertx: Vertx, address: String): HealthService {
        return HealthServiceVertxEBProxy(vertx, address)
    }
}

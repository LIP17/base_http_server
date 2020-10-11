package base_http_server.service.impl

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import proxy.HealthService

class HealthServiceImpl : HealthService {

    override fun isAlive(handler: Handler<AsyncResult<Boolean>>): HealthService {
        handler.handle(Future.succeededFuture(true))
        return this
    }
}

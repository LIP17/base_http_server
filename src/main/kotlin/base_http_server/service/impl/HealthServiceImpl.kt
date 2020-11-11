package base_http_server.service.impl

import com.google.inject.Singleton
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.kotlin.coroutines.awaitResult

@Singleton
class HealthService {

    fun isAlive(handler: Handler<AsyncResult<Boolean>>): HealthService {
        handler.handle(Future.succeededFuture(true))
        return this
    }
}

suspend fun HealthService.isAliveAwait(): Boolean {
    return awaitResult(::isAlive)
}

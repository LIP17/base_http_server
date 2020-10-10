package proxy

import io.vertx.codegen.annotations.Fluent
import io.vertx.codegen.annotations.ProxyGen
import io.vertx.codegen.annotations.VertxGen
import io.vertx.core.AsyncResult
import io.vertx.core.Handler

@ProxyGen
@VertxGen
interface HealthService {

    @Fluent
    fun isAlive(handler: Handler<AsyncResult<Boolean>>): HealthService
}

object HealthServiceFactory {

    


}
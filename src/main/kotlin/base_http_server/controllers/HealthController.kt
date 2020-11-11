package base_http_server.controllers

import base_http_server.service.impl.HealthService
import base_http_server.service.impl.isAliveAwait
import com.google.inject.Inject
import com.google.inject.Singleton
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext

@Singleton
class HealthController @Inject constructor(
    private val healthService: HealthService
) {
    suspend fun healthHandler(context: RoutingContext) {
        val isAlive = healthService.isAliveAwait()
        val response = JsonObject()
        response.put("success", isAlive)
        context.response().statusCode = 200
        context.response().putHeader("Content-Type", "application/json")
        context.response().end(response.encode())
    }
}

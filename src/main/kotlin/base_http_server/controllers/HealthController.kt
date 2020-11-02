package base_http_server.controllers

import base_http_server.service.impl.isAliveAwait
import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import proxy.HealthService

@Singleton
class HealthController @Inject constructor(
    @Named("health-service-impl") private val healthService: HealthService
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
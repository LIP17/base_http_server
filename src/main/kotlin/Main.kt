import io.vertx.core.DeploymentOptions
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.Vertx

object Main : AbstractVerticle() {

    @JvmStatic
    fun main(args: Array<String>) {
        val vertX = Vertx.vertx()
        vertX.rxDeployVerticle(
            "ServerVerticle",
            DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors())
        ).subscribe { it -> println(it) }
    }
}

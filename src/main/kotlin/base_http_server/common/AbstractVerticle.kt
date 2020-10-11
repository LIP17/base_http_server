package base_http_server.common

import io.reactivex.functions.Action
import io.vertx.core.Future
import io.vertx.reactivex.core.AbstractVerticle as RxAbstractVerticle
import java.util.UUID
import java.util.concurrent.TimeUnit

open class AbstractVerticle: RxAbstractVerticle() {
    val verticleId = UUID.randomUUID().toString()

    override fun start(startFuture: Future<Void>) {
        val completableStart = rxStart()
        assert(completableStart != null)

        completableStart.timeout(1, TimeUnit.SECONDS)
            .doOnError {
                println("[${javaClass.simpleName}] Failed deploy $verticleId in deployment ${deploymentID()}")
                startFuture.tryFail(it)
            }
            .doOnComplete{Action {
                startFuture.succeeded() }
            }
            .subscribe {
                println("[${javaClass.simpleName}] Finished deploy $verticleId in deployment ${deploymentID()}")
                startFuture.succeeded()
            }
    }
}
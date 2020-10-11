package base_http_server.common

import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.reactivex.core.AbstractVerticle
import java.util.UUID
import java.util.concurrent.TimeUnit

abstract class BaseVerticle : AbstractVerticle() {
    protected open val startUpTimeoutInSeconds = 10L
    protected val verticleId = UUID.randomUUID().toString()

    override fun start(promise: Promise<Void>) {
        rxStart().timeout(startUpTimeoutInSeconds, TimeUnit.SECONDS)
            .doOnComplete {
                log("verticle deployed")
                promise.complete()
            }
            .doOnError {
                log("verticle deployed")
                promise.fail(it)
            }.subscribe()
    }

    @Deprecated(
        message = "This method is used inside reactivex.AbstractVerticle, but deprecated in core.AbstractVerticle",
        replaceWith = ReplaceWith("start(promise: Promise<Void>)"),
        level = DeprecationLevel.ERROR
    )
    override fun start(startFuture: Future<Void>?) {
        throw UnsupportedOperationException()
    }

    override fun stop(stopPromise: Promise<Void>?) {
        log("verticle stopped")
    }

    @Deprecated(
        message = "This method is used inside reactivex.AbstractVerticle, but deprecated in core.AbstractVerticle",
        replaceWith = ReplaceWith("stop(promise: Promise<Void>)"),
        level = DeprecationLevel.ERROR
    )
    override fun stop(startFuture: Future<Void>?) {
        throw UnsupportedOperationException()
    }

    protected fun log(name: String, vararg pairs: Pair<String, Any>) {
        println("[${javaClass.simpleName}][$verticleId] $name, ${mapOf(*pairs)}")
    }

}
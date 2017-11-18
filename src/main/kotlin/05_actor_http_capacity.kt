import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ActorJob
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay
import java.util.Random

suspend fun actor_http_capacity() {
    val httpRandomNumActor = httpActorWithDelay(2)
    sendPeriodicallyRandomInt(httpRandomNumActor)

    (1..5).forEach {
        delay(100)
        val response = CompletableDeferred<Int?>()
        httpRandomNumActor.send(HttpRequest(response))
        println("Sent request $it")
        println("[$it] --> response ${response.await()}")
    }
}

suspend fun httpActorWithDelay(capacity: Int) = actor<Http>(capacity = capacity) {
    var latestResponse: Int? = null
    for (msg in channel) {
        when (msg) {
            is HttpRequest -> msg.request.complete(latestResponse)
            is HttpResponse -> {
                latestResponse = msg.response
                println("Updated response on [${Thread.currentThread().name}]")
            }
        }
        delay(50)
    }
}

private fun sendPeriodicallyRandomInt(httpRandomNum: ActorJob<Http>): Deferred<Unit> {
    return async {
        while (true) {
            httpRandomNum.send(HttpResponse(Random().nextInt()))
            println("Sent response on [${Thread.currentThread().name}]")
            delay(50)
        }
    }
}
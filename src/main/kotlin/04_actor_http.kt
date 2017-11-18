
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.channels.ActorJob
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import java.util.Random

sealed class Http
data class HttpRequest(val request: CompletableDeferred<Int?>) : Http()
data class HttpResponse(val response: Int) : Http()

suspend fun actor_http() {
    val httpRandomNumActor = httpActor()
    sendPeriodicallyRandomInt(httpRandomNumActor)

    (1..10).forEach {
        delay(10)
        val response = CompletableDeferred<Int?>()
        httpRandomNumActor.send(HttpRequest(response))
        println("Sent request $it")
        println("[$it] --> response ${response.await()}")
    }
}

suspend fun httpActor() = actor<Http>(newFixedThreadPoolContext(3, "http-actor")) {
    var latestResponse: Int? = null
    for (msg in channel) {
        when (msg) {
            is HttpRequest -> msg.request.complete(latestResponse)
            is HttpResponse -> {
                latestResponse = msg.response
                println("Updated response on [${Thread.currentThread().name}]")
            }
        }
    }
}

private fun sendPeriodicallyRandomInt(httpRandomNum: ActorJob<Http>) {
    launch {
        while (true) {
            httpRandomNum.send(HttpResponse(Random().nextInt()))
            println("Sent response on [${Thread.currentThread().name}]")
            delay(30)
        }
    }
}


import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.ClosedReceiveChannelException
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.newSingleThreadContext

suspend fun produce_example() {
    val dotGenerator = produce(newSingleThreadContext("dot-thread")) {
        println("dotGenerator is in thread [${Thread.currentThread().name}]") // working in thread [dot-thread]
        while (isActive) {
            send(".")
            delay(50)
        }
    }

    val dotChannel = dotGenerator.channel
    val dotPyramid = produce {
        println("dotPyramid starting in thread [${Thread.currentThread().name}]") // [commonPool-worker-n]
        (1..10).map {
            async(coroutineContext) {
                println("[$it] in thread [${Thread.currentThread().name}]") // [commonPool-worker-n]
                buildString { (1..it).map { append(dotChannel.receive()) } }
            }
        }.map { send(it.await()) }

        delay(50)
        println("dotPyramid ending in thread [${Thread.currentThread().name}]") // [commonPool-worker-n]
    }

    while (dotPyramid.isActive) {
        try {
            println(dotPyramid.receive())
        } catch (e: ClosedReceiveChannelException) {
            println("The pyramid channel has been closed")
        }
    }
    println("Is dotGenerator active? ${dotGenerator.isActive}")
}
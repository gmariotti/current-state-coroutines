import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import java.nio.channels.AsynchronousFileChannel
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

suspend fun async_example() {

    val sequentialTime = measureTimeMillis {
        val twoSquare = getSquareFromServer(2)
        val threeSquare = getSquareFromServer(3)
        println("2^2 = $twoSquare || 3^2 = $threeSquare")
    }

    val asyncTime = measureTimeMillis {
        val twoSquare = async { getSquareFromServer(2) }
        val threeSquare = async { getSquareFromServer(3) }
        println("2^2 = ${twoSquare.await()} || 3^2 = ${threeSquare.await()}")
    }

    println("Sequential time = $sequentialTime ms")
    println("Async time = $asyncTime ms")
}

suspend fun getSquareFromServer(value: Int): Int {
    delay(1, TimeUnit.SECONDS) // simulate a non-blocking call to the server
    return value * value
}
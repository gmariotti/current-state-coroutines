
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.future.await
import kotlinx.coroutines.experimental.future.future
import kotlinx.coroutines.experimental.newSingleThreadContext

suspend fun future_example() {
    val readString = future(context = newSingleThreadContext("read-string")) {
        stringWithDelay()
    }
    val readString2 = future {
        stringWithDelay()
    }
    println("${readString.await()} - ${readString2.await()}")
}

suspend fun stringWithDelay(): String {
    println("Current thread is [${Thread.currentThread().name}]")
    delay(10)
    println("Current thread is [${Thread.currentThread().name}]")
    return "future"
}
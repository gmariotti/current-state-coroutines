
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.selects.select
import java.util.Random

suspend fun randomNumProducer(id: Int) = produce {
    while (isActive) {
        send("[${Thread.currentThread().name}] - Produce $id")
        val sleep = Random().nextInt(50).toLong()
        delay(sleep)
    }
}

suspend fun classicSuspendingFunction() {
    println("Timeout occurred!!")
}

suspend fun selectBetween(channel1: ReceiveChannel<String>, channel2: ReceiveChannel<String>) {
    select<Unit> {
        channel1.onReceive {
            println(it)
        }
        channel2.onReceiveOrNull {
            println(it ?: "[${Thread.currentThread().name}] - channel is closed")
        }
        onTimeout(20) { classicSuspendingFunction() }
    }
}

suspend fun select_example() {
    val producer1 = randomNumProducer(1)
    val producer2 = randomNumProducer(2)
    repeat(5) {
        selectBetween(producer1, producer2)
    }

    producer2.cancel()
    repeat(3) {
        selectBetween(producer1, producer2)
    }
}
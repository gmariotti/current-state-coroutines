import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {
    val input = args[0].toInt()
    when (input) {
        1 -> classic_example()
        2 -> async_example()
        3 -> produce_example()
        4 -> actor_http()
        5 -> actor_http_capacity()
        6 -> generator()
        7 -> select_example()
        8 -> future_example()
    }
}
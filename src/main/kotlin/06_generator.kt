import kotlin.coroutines.experimental.buildSequence

fun generator() {
    fibonacci().take(5) // Lazily evaluated as expected
    for (num in fibonacci()) {
        if (num == 13) break
    }
}

fun fibonacci() = buildSequence {
    var prev = 1
    println("Current value is $prev")
    yield(prev)

    var next = 1
    while (true) {
        println("Current value is $next")
        val tmp = next
        yield(tmp)
        next = prev + tmp
        prev = tmp
    }
}
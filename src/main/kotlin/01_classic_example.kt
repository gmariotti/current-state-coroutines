import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

suspend fun classic_example() {
    launchAndRepeat()
//    repeatAndLaunch()
}

private suspend fun launchAndRepeat() {
    launch {
        repeat(100_000) {
            println("I'm sleeping $it...")
            delay(500)
        }
    }
    delay(1300)
}

private suspend fun repeatAndLaunch() {
    repeat(100_000) {
        launch {
            print(".")
            delay(500)
//            Thread.sleep(1000)
        }
    }
    delay(1300)
}
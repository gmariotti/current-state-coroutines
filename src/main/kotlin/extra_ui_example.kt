import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.run

val UI = newSingleThreadContext("UI")

interface Image

fun ui_example() {
    launch {
        val image = async { loadImage() }
        val title = async { loadTitle() }
        display(image.await(), title.await())
    }
}

suspend fun display(image: Image, title: String) {
    run(UI) {
        // display on UI thread
    }
}

suspend fun loadImage(): Image {
    delay(1000) // long operation for downloading the image
    return object : Image {}
}

suspend fun loadTitle(): String {
    delay(1000) // long operation for recovering a title
    return "Title"
}

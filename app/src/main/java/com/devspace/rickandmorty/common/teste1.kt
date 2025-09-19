import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun fetchDataWithCallback(): String = suspendCoroutine { continuation ->
    println("Fetching data...")
    Thread {
        Thread.sleep(1000) // Simula uma operação assíncrona
        continuation.resume("Data fetched successfully")
    }.start()
}

fun main() = runBlocking {
    val result = fetchDataWithCallback()
    println(result)
}

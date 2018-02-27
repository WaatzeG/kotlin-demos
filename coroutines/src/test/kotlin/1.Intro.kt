import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import util.TWO_MILLION
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class Intro {

    /**
     * Create 2 million coroutines.
     *
     * Important:
     *  Show commonpool threads
     */
    @Test
    fun create_many_coroutines() {
        val counter = AtomicInteger(0)

        runBlocking {

            val jobs = (1..TWO_MILLION).map {

                // make runBlocking the parent context and use the CommonPool dispatcher
                launch {

                    println("waiting")
                    delay(10L, TimeUnit.SECONDS)

                    val newValue = counter.incrementAndGet()
                    println("${Thread.currentThread().name} : $newValue")
                }

            }

            jobs.forEach { job -> job.join() }
        }
        println("Completed $counter")
    }

    /**
     * Use this function instead of the delay to show custom supsending functions
     */
    suspend fun veryExpensive() {
        delay(10L, TimeUnit.SECONDS)
    }
}

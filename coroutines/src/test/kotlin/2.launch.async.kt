import kotlinx.coroutines.experimental.*
import org.junit.Test
import util.ONE_MILLION
import java.lang.Integer.sum
import java.util.concurrent.TimeUnit

class LaunchAsync {

    /**
     * Show launch fire-and-forget
     */
    @Test
    fun launch_demo() {

        runBlocking {

            val jobs = (1..ONE_MILLION).map {
                launch() {
                    delay(1000L)
                }
            }
            jobs.forEach { job -> job.join() }
        }
    }

    /**
     * Show async and await
     * Catch the results and print reduction
     */
    @Test
    fun async_demo() {

        runBlocking {

            val integers = (1..ONE_MILLION).map { value ->

                async {
                    delay(1000L, TimeUnit.MILLISECONDS)
                    value
                }
            }

            val map = integers.map { it.await() }
            println(map.reduce { acc, i -> sum(acc, i) })
        }
    }

    /**
     * Launch many coroutines and join them all
     */
    @Test
    fun launch_wait_for_many() {

        val jobs = mutableListOf<Job>()

        runBlocking {

            repeat(1000) {
                jobs.add(
                        launch {
                            delay(2, TimeUnit.SECONDS)
                        }
                )
            }
            jobs.forEach { it.join() }
        }
    }
}

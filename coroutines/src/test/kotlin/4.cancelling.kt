import kotlinx.coroutines.experimental.*
import org.junit.Test
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

class Cancelling {

    /**
     * Cancellation is cooperative.
     *  The coroutine does not get cancelled, because it doesn't cooperate
     */
    @Test
    fun cancel_coroutine() {

        runBlocking {

            val launch = launch(coroutineContext + CommonPool) {
                repeat(5) {
                    sleep(1000L) // BLOCKING
                }
                println("Did not get cancelled")
            }
            delay(1, TimeUnit.SECONDS)
            println("Cancelling...")
            launch.cancel()
        }
        println("Done!")
    }

    /**
     * This coroutine cancels
     *  Checks isActive for cancellation
     */
    @Test
    fun cancel_coroutine_cooperate() {

        runBlocking {

            val launch = launch(coroutineContext + CommonPool) {
                while (isActive) {
                    sleep(1000L) // BLOCKING
                }
            }
            delay(1, TimeUnit.SECONDS)
            println("Cancelling...")
            launch.cancel()
        }
        println("Done!")
    }

    /**
     * You can specify a timeout for coroutines
     *  Will throw TimeoutCancellationException
     */
    @Test
    fun cancel_coroutine_with_timeout() {

        runBlocking {

            launch(coroutineContext + CommonPool) {
                try {
                    withTimeout(3, TimeUnit.SECONDS) {
                        repeat(1000) {
                            delay(1000L)
                        }
                    }
                } catch (t: Throwable) {
                    println("Job was cancelled because of timeout: \n$t")
                }
            }
        }
        println("Done!")
    }

}

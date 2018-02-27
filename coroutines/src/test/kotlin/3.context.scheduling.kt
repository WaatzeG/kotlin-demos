
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import util.ONE_MILLION
import java.util.concurrent.atomic.AtomicBoolean

class Context_Scheduling {

    /**
     *  Launch without parent coroutinescope
     *  Will not wait
     */
    @Test
    fun noParentContext() {

        val done = AtomicBoolean(false)

        runBlocking {

            repeat(ONE_MILLION) { count ->

                launch {
                    if (count == ONE_MILLION - 1) {
                        done.set(true)
                    }
                }
            }
        }
        println("Done : ${done.get()}")
    }

    /**
     * Use the CoroutineContext of runBlocking
     *  Coroutines will wait for their children
     *  Children will use their dispatcher too (main thread)
     */
    @Test
    fun useParentContext() {

        val done = AtomicBoolean(false)

        runBlocking {

            repeat(ONE_MILLION) { count ->

                launch(coroutineContext) {
                    if (count == ONE_MILLION - 1) {
                        done.set(true)
                    }
                }
            }
        }
        println("Done : ${done.get()}")
    }


    /**
     * Show different contexts
     */
    @Test
    fun contexts() {

        runBlocking {
            println(Thread.currentThread().name)
        }

        runBlocking {
            launch(coroutineContext) {
                println(Thread.currentThread().name)
            }
        }

        launch {
            println(Thread.currentThread().name)
        }

        /**
         * Use parent context, but with a custom dispatcher
         */
        runBlocking {
            launch(coroutineContext + CommonPool) {
                println(Thread.currentThread().name)
            }
        }
    }
}

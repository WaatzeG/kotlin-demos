import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import java.util.*
import java.util.concurrent.atomic.AtomicLong


val random = Random()

class Channels_Actors {

    /**
     * Channels are like Go channels or BlockingQueues
     *  Have capacity
     *  Support one-to-many and many-to-one producer/consumer
     */
    @Test
    fun channel() {
        val channel = Channel<Int>()

        runBlocking {

            // PRODUCER
            launch {
                while (isActive) {
                    channel.send(random.nextInt())
                }
            }

            // CONSUMER
            launch {
                channel.consumeEach {
                    println(it)
                }
            }

            delay(5000L)
        }
    }

    /**
     * Actor encapsulates a channel and can keep state
     */
    @Test
    fun actor() {

        runBlocking {
            // ACTOR
            val actor = actor<Int> {

                // actors have state
                val sum = AtomicLong(0)

                for (number in channel) {
                    val newSum = sum.addAndGet(number.toLong())
                    println("Sum = $newSum")
                }
            }

            // PRODUCER
            launch {
                while (isActive) {
                    actor.send(random.nextInt())
                }
            }

            delay(5000L)
        }
    }
}

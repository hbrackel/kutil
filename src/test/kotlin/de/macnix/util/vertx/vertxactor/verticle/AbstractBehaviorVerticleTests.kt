package de.macnix.util.vertx.vertxactor.verticle

import arrow.core.Option
import arrow.core.some
import de.macnix.util.vertx.eventbus.EventBusAddress
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.kotlin.coroutines.await
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AbstractBehaviorVerticleTests {
    lateinit var vertx: Vertx
    lateinit var eventBus: EventBus
    val eventBusAddress = EventBusAddress("test.address").some()

    @BeforeEach
    fun launchVertx() {
        vertx = Vertx.vertx()
        eventBus = vertx.eventBus()
    }

    @Test
    fun `an AbstractBehaviorVerticle  with 'sequentialProcessing = false' does not reply in the order of incoming messages, when the handler suspends`() {
        runBlocking(vertx.dispatcher()) {
            val replies = mutableListOf<String>()
            vertx.deployVerticle(AsyncVerticle(eventBusAddress, false)).await()
            eventBusAddress.tap {
                val msg1 =
                    eventBus.request<String>(it.address, "first").onSuccess { msg -> replies.add(msg.body()) }
                val msg2 =
                    eventBus.request<String>(it.address, "second").onSuccess { msg -> replies.add(msg.body()) }

                msg1.await()
                msg2.await()
            }
            assertThat(replies).containsExactly("second", "first")
        }
    }

    @Test
    fun `an AbstractBehaviorVerticle with 'sequentialProcessing = true' does reply in the order of incoming messages, when the handler suspends`() {
        runBlocking(vertx.dispatcher()) {
            val replies = mutableListOf<String>()
            vertx.deployVerticle(AsyncVerticle(eventBusAddress, true)).await()
            eventBusAddress.tap {
                val msg1 =
                    eventBus.request<String>(it.address, "first").onSuccess { msg -> replies.add(msg.body()) }
                val msg2 =
                    eventBus.request<String>(it.address, "second").onSuccess { msg -> replies.add(msg.body()) }

                msg1.await()
                msg2.await()
            }

            assertThat(replies).containsExactly("first", "second")
        }
    }

    @AfterEach
    fun shutdown() {
        vertx.close()
    }
}


class AsyncVerticle(eventBusAddress: Option<EventBusAddress>, sequentialProcessing: Boolean) :
    AbstractBehaviourVerticle<String>(eventBusAddress, sequentialProcessing) {
    override fun createReceive(): Behavior<String> {
        return ReceiveBuilder.buildReceive { msg, thisBehavior ->
            val body = msg.body()
            when (body) {
                "first" -> delay(2000)
                else -> {
                }
            }
            msg.reply(body)
            thisBehavior
        }

    }

    override suspend fun registerMessageCodecs() {
    }

    override suspend fun doAfterStart() {
    }

    override suspend fun doBeforeStop() {
    }
}

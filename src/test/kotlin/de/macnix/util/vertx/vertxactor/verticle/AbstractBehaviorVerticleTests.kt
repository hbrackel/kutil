package de.macnix.util.vertx.vertxactor.verticle

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.deploymentOptionsOf
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
    val eventbusAddress = "test.address"
    val mapper = jacksonObjectMapper()


    @BeforeEach
    fun launchVertx() {
        vertx = Vertx.vertx()
        eventBus = vertx.eventBus()
    }

    @Test
    fun `an AbstractBehaviorVerticle  with 'sequentialProcessing = false' does not reply in the order of incoming messages, when the handler suspends`() {
        runBlocking(vertx.dispatcher()) {
            val options = deploymentOptionsOf(
                config = JsonObject()
                    .put("sequentialProcessing", false)
                    .put("eventBusAddress", eventbusAddress)
            )

            val replies = mutableListOf<String>()
            vertx.deployVerticle(AsynchVerticle(), options).await()
            val msg1 = eventBus.request<String>(eventbusAddress, "first").onSuccess { msg -> replies.add(msg.body()) }
            val msg2 =
                eventBus.request<String>(eventbusAddress, "second").onSuccess { msg -> replies.add(msg.body()) }

            msg1.await()
            msg2.await()

            assertThat(replies).containsExactly("second", "first")
        }
    }

    @Test
    fun `an AbstractBehaviorVerticle with 'sequentialProcessing = true' does reply in the order of incoming messages, when the handler suspends`() {
        runBlocking(vertx.dispatcher()) {
            val options = deploymentOptionsOf(
                config = JsonObject()
                    .put("sequentialProcessing", true)
                    .put("eventBusAddress", eventbusAddress)
            )

            val replies = mutableListOf<String>()
            vertx.deployVerticle(AsynchVerticle(), options).await()
            val msg1 = eventBus.request<String>(eventbusAddress, "first").onSuccess { msg -> replies.add(msg.body()) }
            val msg2 =
                eventBus.request<String>(eventbusAddress, "second").onSuccess { msg -> replies.add(msg.body()) }

            msg1.await()
            msg2.await()

            assertThat(replies).containsExactly("first", "second")
        }
    }

    @AfterEach
    fun shutdown() {
        vertx.close()
    }
}


class AsynchVerticle : AbstractBehaviourVerticle<String>() {
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
}

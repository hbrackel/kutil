package de.macnix.util.vertx.vertxactor.verticle

import arrow.core.None
import arrow.core.Option
import de.macnix.util.vertx.eventbus.EventBusAddress
import io.vertx.core.eventbus.Message
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

abstract class AbstractBehaviourVerticle<T>(
    private val eventBusAddress: Option<EventBusAddress> = None,
    private val sequentialProcessing: Boolean = false
) : CoroutineVerticle(),
    AbstractBehavior<T> {
    private val mailbox: MutableList<Message<T>> by lazy { mutableListOf() }
    protected val logger: Logger = getLogger(javaClass)
    final override lateinit var behavior: Behavior<T>
        private set

    abstract suspend fun registerMessageCodecs()
    abstract suspend fun doAfterStart()
    abstract suspend fun doBeforeStop()

    final override suspend fun start() {
        logger.info("starting {}", javaClass.simpleName)
        registerMessageCodecs()
        logger.info("config[\"eventBusAddress\"]     : {}", eventBusAddress)
        logger.info("config[\"sequentialProcessing\"]: {}", sequentialProcessing)

        behavior = createReceive()
        eventBusAddress.tap {
            vertx.eventBus().consumer<T>(it.address).handler { msg ->
                try {
                    receiveMessage(msg)
                } catch (t: Throwable) {
                    logger.error("received invalid message with body='{}'", msg.body())
                }
            }
        }
        doAfterStart()
    }

    private var processingInProgress = false

    private fun receiveMessage(msg: Message<T>) {
        launch {
            logger.debug("receiveMessage(msg[body]='{}')", msg.body())
            if (sequentialProcessing) {
                mailbox.add(msg)
                if (!processingInProgress) {
                    processingInProgress = true
                    var nextMessage = mailbox.removeFirstOrNull()
                    while (nextMessage != null) {
                        behavior = behavior.receive(nextMessage)
                        nextMessage = mailbox.removeFirstOrNull()
                    }
                    processingInProgress = false
                }
            } else {
                behavior = behavior.receive(msg)
            }
        }
    }

    final override suspend fun stop() {
        logger.info("stopping {}", javaClass.simpleName)
        doBeforeStop()
    }

    companion object {
        const val EVENTBUS_ADDRESS_CFG_KEY = "eventBusAddress"
    }
}

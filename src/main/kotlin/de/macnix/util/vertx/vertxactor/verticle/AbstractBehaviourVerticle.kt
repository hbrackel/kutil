package de.macnix.util.vertx.vertxactor.verticle

import io.vertx.core.eventbus.Message
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

abstract class AbstractBehaviourVerticle<T> : CoroutineVerticle(), AbstractBehavior<T> {
    private var sequentialProcessing: Boolean = false
    private val mailbox: MutableList<Message<T>> by lazy { mutableListOf() }
    lateinit var actorRef: ActorRef
        private set
    protected val logger: Logger = getLogger(javaClass)
    final override lateinit var behavior: Behavior<T>
        private set

    protected lateinit var eventBusAddress:String

    override suspend fun start() {
        logger.info("starting {}", javaClass.simpleName)
        eventBusAddress = config.getString("eventBusAddress", "eventBus://${javaClass.name}-${this.deploymentID}")
        actorRef = ActorRef(eventBusAddress)
        sequentialProcessing = config.getBoolean("sequentialProcessing", false)
        logger.info("config[\"eventBusAddress\"]     : {}", eventBusAddress)
        logger.info("config[\"sequentialProcessing\"]: {}", sequentialProcessing)

        behavior = createReceive()
        vertx.eventBus().consumer<T>(eventBusAddress).handler { msg ->
            try {
                receiveMessage(msg)
            } catch (t: Throwable) {
                logger.error("received invalid message with body='{}'", msg.body())
            }
        }
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

    override suspend fun stop() {
        logger.info("stopping {}", javaClass.simpleName)
    }
}

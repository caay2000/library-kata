package com.github.caay2000.common.eventbus

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging

class EventBus(
    val scope: CoroutineScope = CoroutineScope(Default + CoroutineName("EventBus") + SupervisorJob()),
    private val numPartitions: Int = 3,
) : EventPublisher {
    val logger: KLogger = KotlinLogging.logger {}

    private val _events = List(numPartitions) { MutableSharedFlow<Event>() }
    val events = _events.map { it.asSharedFlow() }

    val subscribers: MutableMap<String, List<EventSubscriber<*>>> = mutableMapOf()

    override fun <EVENT : Event> publish(event: EVENT) {
        runBlocking {
            val partition = Integer.decode("0x${event.aggregateId.last()}") % numPartitions
            logger.trace { "publishing event ${event::class.java.simpleName} into partition $partition" }
            _events[partition].emit(event)
        }
    }

    inline fun <reified EVENT : Event> subscribe(subscriber: EventSubscriber<EVENT>) {
        subscribers[EVENT::class.java.canonicalName] =
            subscribers.getOrDefault(EVENT::class.java.canonicalName, mutableListOf()) + subscriber
    }

    @Suppress("UNCHECKED_CAST")
    fun init() {
        events.forEachIndexed { i, it ->
            scope.launch {
                it.collect {
                    (subscribers[it::class.java.canonicalName] as List<EventSubscriber<Event>>?)?.forEach { subscriber ->
                        logger.trace { "${subscriber::class.java.simpleName} handling event ${it::class.java.simpleName} from partition $i" }
                        subscriber.handle(it)
                    }
                }
            }
        }
    }
}

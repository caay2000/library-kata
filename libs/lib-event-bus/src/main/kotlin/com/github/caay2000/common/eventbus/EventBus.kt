package com.github.caay2000.common.eventbus

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mu.KLogger
import mu.KotlinLogging

class EventBus(
    val scope: CoroutineScope = CoroutineScope(Default + CoroutineName("EventBus") + SupervisorJob()),
    private val numPartitions: Int = 3,
) : EventPublisher {
    val logger: KLogger = KotlinLogging.logger {}

    private val _events = List(numPartitions) { MutableSharedFlow<Event>() }
    val events = _events.map { it.asSharedFlow() }

    private val _partitions: MutableMap<Int, MutableList<Event>> =
        MutableList(numPartitions) { it }
            .associateWith<Int, MutableList<Event>> { mutableListOf() }
            .toMutableMap()

    val partitions: Map<Int, List<Event>>
        get() = _partitions.toMap()

    override fun <EVENT : Event> publish(event: EVENT) {
        scope.launch {
            val partition = Integer.decode("0x${event.aggregateId.last()}") % numPartitions
            logger.debug { "publishing event $event into partition $partition" }
            _events[partition].emit(event)
//            _partitions[partition]!!.add(event)
        }
    }

    inline fun <reified EVENT : Event> subscribe(subscriber: EventSubscriber<EVENT>) {
        events.forEachIndexed { i, it ->
            scope.launch {
                it.filter { it is EVENT }
                    .map { it as EVENT }
                    .collect {
                        logger.debug { "consuming event $it from partition $i" }
                        subscriber.handle(it)
                    }
            }
        }
    }
}

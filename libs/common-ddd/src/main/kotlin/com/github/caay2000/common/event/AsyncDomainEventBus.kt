package com.github.caay2000.common.event

import com.github.caay2000.common.eventbus.EventBus
import mu.KLogger
import mu.KotlinLogging

class AsyncDomainEventBus(override val eventBus: EventBus) : DomainEventBus, DomainEventPublisher {
    private val logger: KLogger = KotlinLogging.logger {}

    override fun <EVENT : DomainEvent> publish(event: EVENT) {
        logger.info { ">> publishing event $event" }
        return eventBus.publish(event)
    }
}

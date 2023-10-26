package com.github.caay2000.common.event

import com.github.caay2000.common.eventbus.EventSubscriber
import mu.KLogger
import org.slf4j.MDC

abstract class DomainEventSubscriber<in EVENT : DomainEvent> : EventSubscriber<EVENT> {

    protected abstract val logger: KLogger

    abstract fun handleEvent(event: EVENT)

    override fun handle(event: EVENT) {
        checkCorrelationId(event)
        logger.info { "processing $event" }
        handleEvent(event)
    }

    private fun checkCorrelationId(event: EVENT) {
        if (event.correlationId != null) {
            MDC.put("correlationId", event.correlationId)
        }
    }
}

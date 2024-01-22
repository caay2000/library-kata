package com.github.caay2000.common.event

import com.github.caay2000.common.eventbus.EventSubscriber
import mu.KLogger
import org.slf4j.MDC
import kotlin.time.measureTime

abstract class DomainEventSubscriber<in EVENT : DomainEvent> : EventSubscriber<EVENT> {
    protected abstract val logger: KLogger

    abstract fun handleEvent(event: EVENT)

    override fun handle(event: EVENT) {
        val duration =
            measureTime {
                checkCorrelationId(event)
                logger.info { ">> processing $event" }
                handleEvent(event)
            }
        logger.info { "<< processed $event in $duration" }
    }

    private fun checkCorrelationId(event: EVENT) {
        if (event.correlationId != null) {
            MDC.put("correlationId", event.correlationId)
        }
    }
}

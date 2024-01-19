package com.github.caay2000.common.event

import org.slf4j.MDC

interface DomainEventPublisher {
    fun <EVENT : DomainEvent> publish(event: EVENT)

    fun <EVENT : DomainEvent> publish(events: List<EVENT>) {
        events.forEach { publish(it.setCorrelationId(MDC.get("correlationId"))) }
    }
}

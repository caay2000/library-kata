package com.github.caay2000.common.event

import arrow.core.Either
import arrow.core.right
import org.slf4j.MDC

interface DomainEventPublisher {

    fun <EVENT : DomainEvent> publish(event: EVENT): Either<Throwable, Unit>
    fun <EVENT : DomainEvent> publish(events: List<EVENT>): Either<Throwable, Unit> {
        val defaultInit: Either<Throwable, Unit> = Unit.right()
        return events.fold(initial = defaultInit) { result, event ->
            if (result.isRight()) {
                publish(event.setCorrelationId(MDC.get("correlationId")))
            } else {
                result
            }
        }
    }
}

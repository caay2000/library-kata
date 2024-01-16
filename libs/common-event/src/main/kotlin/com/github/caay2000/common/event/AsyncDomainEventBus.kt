package com.github.caay2000.common.event

import arrow.core.Either
import com.github.caay2000.common.eventbus.EventBus
import mu.KLogger
import mu.KotlinLogging

class AsyncDomainEventBus(override val eventBus: EventBus) : DomainEventBus, DomainEventPublisher {
    private val logger: KLogger = KotlinLogging.logger {}

    override fun <EVENT : DomainEvent> publish(event: EVENT): Either<Throwable, Unit> {
        logger.info { "publishing event $event" }
        return Either.catch { eventBus.publish(event) }
    }
}

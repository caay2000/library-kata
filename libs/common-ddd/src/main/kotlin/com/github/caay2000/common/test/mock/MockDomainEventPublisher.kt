package com.github.caay2000.common.test.mock

import arrow.core.Either
import arrow.core.right
import com.github.caay2000.common.event.DomainEvent
import com.github.caay2000.common.event.DomainEventPublisher

// TODO this should be moved to common-test when needed
class MockDomainEventPublisher : DomainEventPublisher {
    private val _events: MutableList<DomainEvent> = mutableListOf()
    val events: List<DomainEvent>
        get() = _events.toList()

    override fun <EVENT : DomainEvent> publish(event: EVENT): Either<Throwable, Unit> {
        _events.add(event)
        return Unit.right()
    }
}

package com.github.caay2000.common.ddd

import com.github.caay2000.common.event.DomainEvent

abstract class Aggregate {
    private var events: MutableSet<DomainEvent> = mutableSetOf()

    abstract val id: AggregateId

    fun pullEvents(): List<DomainEvent> {
        val pulledEvents: MutableSet<DomainEvent> = events
        events = mutableSetOf()
        return pulledEvents.toList()
    }

    fun <E : DomainEvent> pushEvent(event: E) {
        this.events.add(event)
    }

    fun <E : DomainEvent> pushEvents(events: List<E>) {
        this.events.addAll(events)
    }
}

interface AggregateId

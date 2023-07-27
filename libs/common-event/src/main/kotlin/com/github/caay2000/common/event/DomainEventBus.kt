package com.github.caay2000.common.event

import com.github.caay2000.eventbus.EventBus

interface DomainEventBus {

    val eventBus: EventBus
}

inline fun <reified EVENT : DomainEvent> DomainEventBus.subscribe(subscriber: DomainEventSubscriber<EVENT>): DomainEventBus {
    this.eventBus.subscribe(subscriber)
    return this
}

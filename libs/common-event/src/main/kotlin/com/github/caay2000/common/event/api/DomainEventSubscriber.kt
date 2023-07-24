package com.github.caay2000.common.event.api

import com.github.caay2000.eventbus.EventSubscriber
import mu.KLogger

abstract class DomainEventSubscriber<in EVENT : DomainEvent> : EventSubscriber<EVENT> {

    protected abstract val logger: KLogger

    abstract fun handleEvent(event: EVENT)

    override fun handle(event: EVENT) {
        this.logger.info { "consuming $event" }
        handleEvent(event)
    }
}

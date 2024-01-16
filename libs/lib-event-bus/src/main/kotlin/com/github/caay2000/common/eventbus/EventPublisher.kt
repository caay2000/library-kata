package com.github.caay2000.common.eventbus

interface EventPublisher {
    fun <EVENT : Event> publish(event: EVENT)
}

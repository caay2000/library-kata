package com.github.caay2000.eventbus

interface EventPublisher {

    fun <EVENT : Event> publish(event: EVENT)
}

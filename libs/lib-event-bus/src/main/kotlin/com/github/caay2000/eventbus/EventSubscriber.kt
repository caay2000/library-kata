package com.github.caay2000.eventbus

interface EventSubscriber<in EVENT : Event> {

    fun handle(event: EVENT)
}

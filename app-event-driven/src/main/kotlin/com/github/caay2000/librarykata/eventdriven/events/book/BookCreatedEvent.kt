package com.github.caay2000.librarykata.eventdriven.events.book

import com.github.caay2000.common.event.DomainEvent

data class BookCreatedEvent(
    val bookId: String,
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
) : DomainEvent(bookId)

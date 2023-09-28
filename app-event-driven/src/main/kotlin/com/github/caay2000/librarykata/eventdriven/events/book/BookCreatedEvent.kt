package com.github.caay2000.librarykata.eventdriven.events.book

import com.github.caay2000.common.event.DomainEvent
import java.util.UUID

data class BookCreatedEvent(
    val bookId: UUID,
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
) : DomainEvent(bookId)

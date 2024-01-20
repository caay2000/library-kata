package com.github.caay2000.librarykata.eventdriven.context.loan.book.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.create.CreateBookCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.create.CreateBookCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.events.book.BookCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class CreateBookOnBookCreatedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<BookCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = CreateBookCommandHandler(bookRepository)

    override fun handleEvent(event: BookCreatedEvent) {
        commandHandler.invoke(CreateBookCommand(BookId(event.bookId), BookIsbn(event.isbn)))
    }
}

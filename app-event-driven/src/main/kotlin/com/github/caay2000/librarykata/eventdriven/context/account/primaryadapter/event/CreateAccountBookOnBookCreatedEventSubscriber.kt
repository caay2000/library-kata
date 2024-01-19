package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.account.application.book.create.CreateBookCommand
import com.github.caay2000.librarykata.eventdriven.context.account.application.book.create.CreateBookCommandHandler
import com.github.caay2000.librarykata.eventdriven.events.book.BookCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class CreateAccountBookOnBookCreatedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<BookCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = CreateBookCommandHandler(bookRepository)

    override fun handleEvent(event: BookCreatedEvent) {
        commandHandler.invoke(
            CreateBookCommand(
                id = event.bookId,
                isbn = event.isbn,
                title = event.title,
                author = event.author,
                pages = event.pages,
                publisher = event.publisher,
            ),
        )
    }
}

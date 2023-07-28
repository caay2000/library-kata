package com.github.caay2000.projectskeleton.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.projectskeleton.context.loan.application.BookRepository
import com.github.caay2000.projectskeleton.context.loan.application.book.create.CreateBookCommand
import com.github.caay2000.projectskeleton.context.loan.application.book.create.CreateBookCommandHandler
import com.github.caay2000.projectskeleton.events.book.BookCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class CreateBookOnBookCreatedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<BookCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = CreateBookCommandHandler(bookRepository)

    override fun handleEvent(event: BookCreatedEvent) {
        commandHandler.invoke(CreateBookCommand(event.bookId, event.isbn))
    }
}

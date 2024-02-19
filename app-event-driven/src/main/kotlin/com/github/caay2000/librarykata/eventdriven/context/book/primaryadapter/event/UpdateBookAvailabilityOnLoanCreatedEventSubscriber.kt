package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.book.application.update.UpdateBookAvailabilityCommand
import com.github.caay2000.librarykata.eventdriven.context.book.application.update.UpdateBookAvailabilityCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class UpdateBookAvailabilityOnLoanCreatedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<LoanCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateBookAvailabilityCommandHandler(bookRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(UpdateBookAvailabilityCommand(BookId(event.bookId), false))
    }
}
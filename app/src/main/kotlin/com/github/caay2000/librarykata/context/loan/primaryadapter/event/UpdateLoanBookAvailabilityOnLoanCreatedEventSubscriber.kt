package com.github.caay2000.librarykata.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.context.loan.application.BookRepository
import com.github.caay2000.librarykata.context.loan.application.book.availability.UpdateBookAvailabilityCommand
import com.github.caay2000.librarykata.context.loan.application.book.availability.UpdateBookAvailabilityCommandHandler
import com.github.caay2000.librarykata.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class UpdateLoanBookAvailabilityOnLoanCreatedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<LoanCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateBookAvailabilityCommandHandler(bookRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(UpdateBookAvailabilityCommand(event.bookId, false))
    }
}

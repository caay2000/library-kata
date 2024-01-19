package com.github.caay2000.librarykata.event.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.event.context.loan.application.BookRepository
import com.github.caay2000.librarykata.event.context.loan.application.book.availability.UpdateBookAvailabilityCommand
import com.github.caay2000.librarykata.event.context.loan.application.book.availability.UpdateBookAvailabilityCommandHandler
import com.github.caay2000.librarykata.event.events.loan.LoanFinishedEvent
import mu.KLogger
import mu.KotlinLogging

class UpdateLoanBookAvailabilityOnLoanFinishedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<LoanFinishedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateBookAvailabilityCommandHandler(bookRepository)

    override fun handleEvent(event: LoanFinishedEvent) {
        commandHandler.invoke(UpdateBookAvailabilityCommand(event.bookId, true))
    }
}

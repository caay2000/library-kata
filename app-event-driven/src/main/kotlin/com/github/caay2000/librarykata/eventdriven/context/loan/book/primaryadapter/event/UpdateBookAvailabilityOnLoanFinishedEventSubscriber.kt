package com.github.caay2000.librarykata.eventdriven.context.loan.book.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.availability.UpdateBookAvailabilityCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.book.application.availability.UpdateBookAvailabilityCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanFinishedEvent
import mu.KLogger
import mu.KotlinLogging

class UpdateBookAvailabilityOnLoanFinishedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<LoanFinishedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateBookAvailabilityCommandHandler(bookRepository)

    override fun handleEvent(event: LoanFinishedEvent) {
        commandHandler.invoke(UpdateBookAvailabilityCommand(BookId(event.bookId), true))
    }
}

package com.github.caay2000.projectskeleton.context.book.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.application.availability.UpdateBookAvailabilityCommand
import com.github.caay2000.projectskeleton.context.book.application.availability.UpdateBookAvailabilityCommandHandler
import com.github.caay2000.projectskeleton.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class UpdateBookAvailabilityOnLoanCreatedEventSubscriber(bookRepository: BookRepository) : DomainEventSubscriber<LoanCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = UpdateBookAvailabilityCommandHandler(bookRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(UpdateBookAvailabilityCommand(event.bookId, false))
    }
}

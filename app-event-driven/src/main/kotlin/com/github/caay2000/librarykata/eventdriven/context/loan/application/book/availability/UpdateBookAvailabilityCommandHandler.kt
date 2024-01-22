package com.github.caay2000.librarykata.eventdriven.context.loan.application.book.availability

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository
import mu.KLogger
import mu.KotlinLogging

class UpdateBookAvailabilityCommandHandler(bookRepository: BookRepository) : CommandHandler<UpdateBookAvailabilityCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val updater = AvailabilityUpdater(bookRepository)

    override fun handle(command: UpdateBookAvailabilityCommand): Unit = updater.invoke(command.bookId, BookAvailable(command.available)).let { }
}

data class UpdateBookAvailabilityCommand(val bookId: BookId, val available: Boolean) : Command

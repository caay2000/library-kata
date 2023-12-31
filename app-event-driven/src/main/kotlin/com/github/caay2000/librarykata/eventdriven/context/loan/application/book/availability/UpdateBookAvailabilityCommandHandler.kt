package com.github.caay2000.librarykata.eventdriven.context.loan.application.book.availability

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookAvailable
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class UpdateBookAvailabilityCommandHandler(bookRepository: BookRepository) : CommandHandler<UpdateBookAvailabilityCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val updater = BookAvailabilityUpdater(bookRepository)

    override fun handle(command: UpdateBookAvailabilityCommand): Unit =
        updater.invoke(
            bookId = BookId(command.bookId),
            available = BookAvailable(command.availability),
        ).getOrThrow()
}

data class UpdateBookAvailabilityCommand(val bookId: UUID, val availability: Boolean) : Command

package com.github.caay2000.projectskeleton.context.book.application.availability

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.domain.BookAvailable
import com.github.caay2000.projectskeleton.context.book.domain.BookId
import java.util.UUID

class UpdateBookAvailabilityCommandHandler(bookRepository: BookRepository) : CommandHandler<UpdateBookAvailabilityCommand> {

    private val updater = BookAvailabilityUpdater(bookRepository)

    override fun invoke(command: UpdateBookAvailabilityCommand): Unit =
        updater.invoke(
            bookId = BookId(command.bookId),
            available = BookAvailable(command.availability),
        ).getOrThrow()
}

data class UpdateBookAvailabilityCommand(val bookId: UUID, val availability: Boolean) : Command

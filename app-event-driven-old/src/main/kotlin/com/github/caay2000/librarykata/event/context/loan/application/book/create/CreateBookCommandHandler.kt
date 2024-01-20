package com.github.caay2000.librarykata.event.context.loan.application.book.create

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.event.context.loan.application.BookRepository
import com.github.caay2000.librarykata.event.context.loan.domain.BookId
import com.github.caay2000.librarykata.event.context.loan.domain.BookIsbn
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class CreateBookCommandHandler(bookRepository: BookRepository) : CommandHandler<CreateBookCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = BookCreator(bookRepository)

    override fun handle(command: CreateBookCommand): Unit =
        creator.invoke(
            bookId = BookId(command.bookId),
            isbn = BookIsbn(command.isbn),
        )
}

data class CreateBookCommand(val bookId: UUID, val isbn: String) : Command
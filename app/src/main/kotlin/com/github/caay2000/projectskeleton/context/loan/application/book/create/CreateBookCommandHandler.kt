package com.github.caay2000.projectskeleton.context.loan.application.book.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.loan.application.BookRepository
import com.github.caay2000.projectskeleton.context.loan.domain.BookId
import com.github.caay2000.projectskeleton.context.loan.domain.BookIsbn
import java.util.UUID

class CreateBookCommandHandler(bookRepository: BookRepository) : CommandHandler<CreateBookCommand> {

    private val creator = BookCreator(bookRepository)

    override fun invoke(command: CreateBookCommand): Unit =
        creator.invoke(
            bookId = BookId(command.bookId),
            isbn = BookIsbn(command.isbn),
        ).getOrThrow()
}

data class CreateBookCommand(val bookId: UUID, val isbn: String) : Command

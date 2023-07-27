package com.github.caay2000.projectskeleton.context.book.application.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.projectskeleton.common.cqrs.Command
import com.github.caay2000.projectskeleton.common.cqrs.CommandHandler
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.domain.BookAuthor
import com.github.caay2000.projectskeleton.context.book.domain.BookId
import com.github.caay2000.projectskeleton.context.book.domain.BookIsbn
import com.github.caay2000.projectskeleton.context.book.domain.BookPages
import com.github.caay2000.projectskeleton.context.book.domain.BookPublisher
import com.github.caay2000.projectskeleton.context.book.domain.BookTitle
import com.github.caay2000.projectskeleton.context.book.domain.CreateBookRequest
import java.util.UUID

class CreateBookCommandHandler(
    bookRepository: BookRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<CreateBookCommand> {

    private val creator = BookCreator(bookRepository, eventPublisher)

    override fun invoke(command: CreateBookCommand): Unit =
        creator.invoke(command.toCreateBookRequest()).getOrThrow()

    private fun CreateBookCommand.toCreateBookRequest() =
        CreateBookRequest(
            id = BookId(id),
            isbn = BookIsbn(this.isbn),
            title = BookTitle(this.title),
            author = BookAuthor(this.author),
            pages = BookPages(this.pages),
            publisher = BookPublisher(this.publisher),
        )
}

data class CreateBookCommand(
    val id: UUID,
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
) : Command

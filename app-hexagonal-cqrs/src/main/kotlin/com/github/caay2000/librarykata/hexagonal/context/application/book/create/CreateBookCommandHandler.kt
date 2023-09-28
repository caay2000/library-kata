package com.github.caay2000.librarykata.hexagonal.context.application.book.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAuthor
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPages
import com.github.caay2000.librarykata.hexagonal.context.domain.BookPublisher
import com.github.caay2000.librarykata.hexagonal.context.domain.BookTitle
import com.github.caay2000.librarykata.hexagonal.context.domain.CreateBookRequest
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class CreateBookCommandHandler(
    bookRepository: BookRepository,
) : CommandHandler<CreateBookCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = BookCreator(bookRepository)

    override fun handle(command: CreateBookCommand): Unit =
        creator.invoke(command.toCreateBookRequest()).getOrThrow()

    private fun CreateBookCommand.toCreateBookRequest() =
        CreateBookRequest(
            id = BookId(id.toString()),
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

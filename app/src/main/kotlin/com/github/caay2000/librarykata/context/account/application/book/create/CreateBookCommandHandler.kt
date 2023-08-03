package com.github.caay2000.librarykata.context.account.application.book.create

import arrow.core.getOrElse
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.context.account.application.BookRepository
import com.github.caay2000.librarykata.context.account.domain.BookAuthor
import com.github.caay2000.librarykata.context.account.domain.BookId
import com.github.caay2000.librarykata.context.account.domain.BookIsbn
import com.github.caay2000.librarykata.context.account.domain.BookPages
import com.github.caay2000.librarykata.context.account.domain.BookPublisher
import com.github.caay2000.librarykata.context.account.domain.BookTitle
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class CreateBookCommandHandler(bookRepository: BookRepository) : CommandHandler<CreateBookCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = BookCreator(bookRepository)

    override fun handle(command: CreateBookCommand): Unit =
        creator.invoke(
            id = BookId(command.id),
            isbn = BookIsbn(command.isbn),
            title = BookTitle(command.title),
            author = BookAuthor(command.author),
            pages = BookPages(command.pages),
            publisher = BookPublisher(command.publisher),
        ).getOrElse {
            if (it is BookCreatorError.BookAlreadyExists) {
                Unit
            } else {
                throw it
            }
        }
}

data class CreateBookCommand(
    val id: UUID,
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
) : Command

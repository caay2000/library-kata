package com.github.caay2000.librarykata.eventdriven.context.loan.book.application.create

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository
import mu.KLogger
import mu.KotlinLogging

class CreateBookCommandHandler(bookRepository: BookRepository) : CommandHandler<CreateBookCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = BookCreator(bookRepository)

    override fun handle(command: CreateBookCommand): Unit = creator.invoke(command.bookId, command.bookIsbn)
}

data class CreateBookCommand(val bookId: BookId, val bookIsbn: BookIsbn) : Command

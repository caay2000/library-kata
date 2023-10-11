package com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.book

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.librarykata.hexagonalarrow.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonalarrow.context.application.book.create.CreateBookCommand
import com.github.caay2000.librarykata.hexagonalarrow.context.application.book.create.CreateBookCommandHandler
import com.github.caay2000.librarykata.hexagonalarrow.context.application.book.find.FindBookByIdQuery
import com.github.caay2000.librarykata.hexagonalarrow.context.application.book.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.BookId
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.BookCreateRequestDocument
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.toBookByIdDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class CreateBookController(
    private val idGenerator: IdGenerator,
    bookRepository: BookRepository,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateBookCommandHandler(bookRepository)
    private val queryHandler = FindBookByIdQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<BookCreateRequestDocument>()
        val bookId = idGenerator.generate()
        commandHandler.invoke(request.toCreateBookCommand(bookId))

        val queryResponse = queryHandler.invoke(FindBookByIdQuery(BookId(bookId)))
        call.respond(HttpStatusCode.Created, queryResponse.value.toBookByIdDocument())
    }

    private fun BookCreateRequestDocument.toCreateBookCommand(bookId: String) =
        CreateBookCommand(
            id = UUID.fromString(bookId),
            isbn = isbn,
            title = title,
            author = author,
            pages = pages,
            publisher = publisher,
        )
}

package com.github.caay2000.projectskeleton.context.book.primaryadapter.http

import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.application.create.CreateBookCommand
import com.github.caay2000.projectskeleton.context.book.application.create.CreateBookCommandHandler
import com.github.caay2000.projectskeleton.context.book.application.find.FindBookByIdQuery
import com.github.caay2000.projectskeleton.context.book.application.find.FindBookByIdQueryHandler
import com.github.caay2000.projectskeleton.context.book.domain.BookId
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.BookCreateRequestDocument
import com.github.caay2000.projectskeleton.context.book.primaryadapter.http.serialization.toBookByIdDocument
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
    eventPublisher: DomainEventPublisher,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateBookCommandHandler(bookRepository, eventPublisher)
    private val queryHandler = FindBookByIdQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<BookCreateRequestDocument>()
        val bookId = UUID.fromString(idGenerator.generate())
        commandHandler.invoke(request.toCreateBookCommand(bookId))

        val queryResponse = queryHandler.handle(FindBookByIdQuery(BookId(bookId)))
        call.respond(HttpStatusCode.Created, queryResponse.value.toBookByIdDocument())
    }

    private fun BookCreateRequestDocument.toCreateBookCommand(bookId: UUID) =
        CreateBookCommand(
            id = bookId,
            isbn = isbn,
            title = title,
            author = author,
            pages = pages,
            publisher = publisher,
        )
}

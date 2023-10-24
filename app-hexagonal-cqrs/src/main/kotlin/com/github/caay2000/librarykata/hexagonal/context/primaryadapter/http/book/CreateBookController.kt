package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.ErrorResponseDocument
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.context.book.BookByIdResource
import com.github.caay2000.common.jsonapi.context.book.BookRequestResource
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.create.CreateBookCommand
import com.github.caay2000.librarykata.hexagonal.context.application.book.create.CreateBookCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocument
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
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
        val request = call.receive<JsonApiRequestDocument<BookRequestResource>>()
        val bookId = idGenerator.generate()
        commandHandler.invoke(request.toCreateBookCommand(bookId))

        val queryResponse = queryHandler.invoke(FindBookByIdQuery(BookId(bookId)))
        call.respond(HttpStatusCode.Created, queryResponse.value.toJsonApiDocument())
    }

    private fun JsonApiRequestDocument<BookRequestResource>.toCreateBookCommand(bookId: String) =
        CreateBookCommand(
            id = UUID.fromString(bookId),
            isbn = data.attributes.isbn,
            title = data.attributes.title,
            author = data.attributes.author,
            pages = data.attributes.pages,
            publisher = data.attributes.publisher,
        )

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Book")
            description = "Create Book"
            request {
                body<JsonApiRequestDocument<BookRequestResource>> {
                    mediaType(ContentType.JsonApi)
                    required = true
                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Book Created"
                    body<JsonApiDocument<BookByIdResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                HttpStatusCode.BadRequest to {
                    description = "Error creating Book"
                    body<ErrorResponseDocument> {
                        mediaType(ContentType.JsonApi)
                        example("BookAlreadyExists", "Book {bookId} already exists")
                    }
                }
                HttpStatusCode.InternalServerError to { description = "Something unexpected happened" }
            }
        }
    }
}

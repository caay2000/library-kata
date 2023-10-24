package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.ErrorResponseDocument
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.context.book.BookByIdResource
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.BookFinderError
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocument
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindBookController(bookRepository: BookRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = FindBookByIdQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val bookId = BookId(UUID.fromString(call.parameters["id"]!!).toString())

        val queryResponse = queryHandler.invoke(FindBookByIdQuery(bookId))
        call.respond(HttpStatusCode.OK, queryResponse.value.toJsonApiDocument())
    }

    override suspend fun handleExceptions(call: ApplicationCall, e: Exception) {
        call.serverError {
            when (e) {
                is BookFinderError.BookNotFoundError -> ServerResponse(HttpStatusCode.NotFound, "BookNotFoundError", e.message)
                else -> ServerResponse(HttpStatusCode.InternalServerError, "Unknown Error", e.message)
            }
        }
    }

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Book")
            description = "Find Book"
            request {
                pathParameter<String>("id") {
                    description = "Account Id"
                    required = true
                    example = "00000000-0000-0000-0000-000000000000"
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Book Information"
                    body<JsonApiDocument<BookByIdResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                HttpStatusCode.NotFound to {
                    description = "Error finding Book"
                    body<ErrorResponseDocument> {
                        mediaType(ContentType.JsonApi)
                        example("BookNotFoundError", "Book {bookId} not found")
                    }
                }
                HttpStatusCode.InternalServerError to { description = "Something unexpected happened" }
            }
        }
    }
}

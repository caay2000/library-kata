package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.RequestInclude
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.BookFinderError
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer.BookToBookDocumentTransformer
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindBookController(bookRepository: BookRepository, loanRepository: LoanRepository) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = FindBookByIdQueryHandler(bookRepository)
    private val transformer: Transformer<Book, JsonApiDocument<BookResource>> = BookToBookDocumentTransformer(loanRepository)

    internal enum class Included : RequestInclude { LOANS }

    override suspend fun handle(call: ApplicationCall) {
        val bookId = BookId(UUID.fromString(call.parameters["id"]!!).toString())
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResponse = queryHandler.invoke(FindBookByIdQuery(bookId))
        val responseDocument = transformer.invoke(queryResponse.book, jsonApiParams.include)
        call.respond(HttpStatusCode.OK, responseDocument)
    }

    override suspend fun handleExceptions(
        call: ApplicationCall,
        e: Exception,
    ) {
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
                    body<JsonApiDocument<BookResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.NotFound,
                    summary = "Error finding Book",
                    responseExample("BookNotFoundError", "Book {bookId} not found"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                    responseExample("UnknownError", "message with information about the error"),
                )
            }
        }
    }
}

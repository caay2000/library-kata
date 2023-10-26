package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestParams
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.application.book.search.SearchBooksQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.search.SearchBooksQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiListDocument
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging

class SearchBookController(bookRepository: BookRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = SearchBooksQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val params = call.parameters.toMap().toJsonApiRequestParams()
        val books = queryHandler.invoke(params.toQuery())
        val document = books.values.toJsonApiListDocument()
        call.respond(HttpStatusCode.OK, document)
    }

    private fun JsonApiRequestParams.toQuery() =
        when {
            filter.containsKey("isbn") -> SearchBooksQuery.SearchAllBooksByIsbnQuery(filter["isbn"]!!.first())
            else -> SearchBooksQuery.SearchAllBooksQuery
        }

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Book")
            description = "Search Books"
            request {
                queryParameter<String>("filter[isbn]") {
                    description = "Filter by Book ISBN"
                    required = false
                    example = "00000000-0000-0000-0000-000000000000"
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Books retrieved"
                    body<JsonApiListDocument<BookGroupResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                    responseExample("UnknownError", "message with information about the error"),
                )
            }
        }
    }
}

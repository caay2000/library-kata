package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiRequestParams
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.eventdriven.context.book.application.search.SearchBookQuery
import com.github.caay2000.librarykata.eventdriven.context.book.application.search.SearchBookQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.book.application.search.SearchBookQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer.BookGroupDocumentListTransformer
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

    private val queryHandler: QueryHandler<SearchBookQuery, SearchBookQueryResponse> = SearchBookQueryHandler(bookRepository)
    private val transformer: Transformer<List<Book>, JsonApiDocumentList<BookGroupResource>> = BookGroupDocumentListTransformer()

    override suspend fun handle(call: ApplicationCall) {
        val jsonApiParams = call.parameters.toMap().toJsonApiRequestParams()
        val books = queryHandler.invoke(jsonApiParams.toQuery())
        val response = transformer.invoke(books.values)
        call.respond(HttpStatusCode.OK, response)
    }

    private fun JsonApiRequestParams.toQuery() =
        when {
            filter.containsKey("isbn") -> SearchBookQuery.SearchAllBookByIsbnQuery(filter["isbn"]!!.first())
            else -> SearchBookQuery.SearchAllBookQuery
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
                    body<JsonApiDocumentList<BookGroupResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

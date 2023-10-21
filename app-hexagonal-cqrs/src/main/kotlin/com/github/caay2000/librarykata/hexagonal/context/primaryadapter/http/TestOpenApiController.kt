package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http

import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.search.SearchAllBooksQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.search.SearchAllBooksQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiListDocument
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging

class TestOpenApiController(bookRepository: BookRepository) : OpenApiController {

    private val queryHandler = SearchAllBooksQueryHandler(bookRepository)

    override val logger: KLogger = KotlinLogging.logger {}

    override val method: HttpMethod = HttpMethod.Get
    override val path: String = "/test"

    override fun NotarizedRoute.Config.documentation() {
        tags = setOf("test")
        post = PostInfo.builder {
            summary("test post summary")
            description("test post decriptor")
            request {
                requestType<Unit>()
                description("a test request for open api")
            }
            response {
                responseCode(HttpStatusCode.NoContent)
                responseType<Unit>()
                description("TestModules.defaultResponseDescription")
            }
        }
    }

    override suspend fun handle(call: ApplicationCall) {
        val books = queryHandler.invoke(SearchAllBooksQuery())
        val document = books.values.toJsonApiListDocument()
        call.respond(HttpStatusCode.OK, document)
    }
}

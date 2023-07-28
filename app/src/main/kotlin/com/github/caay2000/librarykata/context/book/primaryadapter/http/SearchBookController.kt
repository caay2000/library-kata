package com.github.caay2000.librarykata.context.book.primaryadapter.http

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.context.book.application.BookRepository
import com.github.caay2000.librarykata.context.book.application.search.SearchAllBooksQuery
import com.github.caay2000.librarykata.context.book.application.search.SearchAllBooksQueryHandler
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.toAllBooksDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging

class SearchBookController(bookRepository: BookRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = SearchAllBooksQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val books = queryHandler.handle(SearchAllBooksQuery())
        val document = books.values.toAllBooksDocument()
        call.respond(HttpStatusCode.OK, document)
    }
}

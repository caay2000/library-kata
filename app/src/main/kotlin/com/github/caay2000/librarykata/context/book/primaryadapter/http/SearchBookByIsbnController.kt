package com.github.caay2000.librarykata.context.book.primaryadapter.http

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.context.book.application.BookRepository
import com.github.caay2000.librarykata.context.book.application.search.SearchBookByIsbnQuery
import com.github.caay2000.librarykata.context.book.application.search.SearchBookByIsbnQueryHandler
import com.github.caay2000.librarykata.context.book.domain.BookIsbn
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.toBookDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging

class SearchBookByIsbnController(bookRepository: BookRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = SearchBookByIsbnQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val isbn = BookIsbn(call.parameters["isbn"]!!)

        val queryResponse = queryHandler.invoke(SearchBookByIsbnQuery(isbn))
        call.respond(HttpStatusCode.OK, queryResponse.value.toBookDocument())
    }
}

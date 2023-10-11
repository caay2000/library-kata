package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.search.SearchAllBooksQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.search.SearchAllBooksQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toBookByIsbnListDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging

class SearchBookController(bookRepository: BookRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = SearchAllBooksQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val books = queryHandler.invoke(SearchAllBooksQuery())
        val document = books.values.toBookByIsbnListDocument()
        call.respond(HttpStatusCode.OK, document)
    }
}

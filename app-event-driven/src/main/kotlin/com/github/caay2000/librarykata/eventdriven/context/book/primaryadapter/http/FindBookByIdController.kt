package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.eventdriven.context.book.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookByIdQuery
import com.github.caay2000.librarykata.eventdriven.context.book.application.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.serialization.toBookByIdDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindBookByIdController(bookRepository: BookRepository) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = FindBookByIdQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val bookId = BookId(UUID.fromString(call.parameters["id"]!!))

        val queryResponse = queryHandler.invoke(FindBookByIdQuery(bookId))
        call.respond(HttpStatusCode.OK, queryResponse.value.toBookByIdDocument())
    }
}

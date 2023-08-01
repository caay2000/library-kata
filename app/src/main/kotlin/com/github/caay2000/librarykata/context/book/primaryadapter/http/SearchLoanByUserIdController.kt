package com.github.caay2000.librarykata.context.book.primaryadapter.http

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.context.book.application.BookRepository
import com.github.caay2000.librarykata.context.book.application.LoanRepository
import com.github.caay2000.librarykata.context.book.application.find.FindBookByIdQuery
import com.github.caay2000.librarykata.context.book.application.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.context.book.application.loan.search.SearchLoanByUserIdQuery
import com.github.caay2000.librarykata.context.book.application.loan.search.SearchLoanByUserIdQueryHandler
import com.github.caay2000.librarykata.context.book.domain.UserId
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.LoanByUserIdDocument
import com.github.caay2000.librarykata.context.book.primaryadapter.http.serialization.toLoanDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class SearchLoanByUserIdController(
    bookRepository: BookRepository,
    loanRepository: LoanRepository,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val loanQueryHandler = SearchLoanByUserIdQueryHandler(loanRepository)
    private val bookQueryHandler = FindBookByIdQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val userId = UserId(UUID.fromString(call.parameters["userId"]!!))

        val loans = loanQueryHandler.handle(SearchLoanByUserIdQuery(userId)).value

        val document = LoanByUserIdDocument(
            userId = userId.value,
            loans = loans.map { loan ->
                loan.toLoanDocument(bookQueryHandler.handle(FindBookByIdQuery(loan.bookId)).value)
            },
        )

        call.respond(HttpStatusCode.OK, document)
    }
}

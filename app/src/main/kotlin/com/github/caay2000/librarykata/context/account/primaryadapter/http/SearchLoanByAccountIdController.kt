package com.github.caay2000.librarykata.context.account.primaryadapter.http

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.context.account.application.BookRepository
import com.github.caay2000.librarykata.context.account.application.LoanRepository
import com.github.caay2000.librarykata.context.account.application.book.find.FindBookByIdQuery
import com.github.caay2000.librarykata.context.account.application.book.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.context.account.application.loan.search.SearchLoanByAccountIdQuery
import com.github.caay2000.librarykata.context.account.application.loan.search.SearchLoanByUserIdQueryHandler
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.primaryadapter.http.serialization.LoanByUserIdDocument
import com.github.caay2000.librarykata.context.account.primaryadapter.http.serialization.toLoanDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class SearchLoanByAccountIdController(
    bookRepository: BookRepository,
    loanRepository: LoanRepository,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val loanQueryHandler = SearchLoanByUserIdQueryHandler(loanRepository)
    private val bookQueryHandler = FindBookByIdQueryHandler(bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!))

        val loans = loanQueryHandler.handle(SearchLoanByAccountIdQuery(accountId)).value

        val document = LoanByUserIdDocument(
            accountId = accountId.value,
            loans = loans.map { loan ->
                loan.toLoanDocument(bookQueryHandler.handle(FindBookByIdQuery(loan.bookId)).value)
            },
        )

        call.respond(HttpStatusCode.OK, document)
    }
}

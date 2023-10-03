package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.book.find.FindBookByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByUserIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanByAccountIdDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toLoanDocument
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
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!).toString())

        val loans = loanQueryHandler.invoke(SearchLoanByAccountIdQuery(accountId)).value

        val document = LoanByAccountIdDocument(
            accountId = UUID.fromString(accountId.value),
            loans = loans.map { loan ->
                loan.toLoanDocument(bookQueryHandler.invoke(FindBookByIdQuery(loan.bookId)).value)
            },
        )

        call.respond(HttpStatusCode.OK, document)
    }
}

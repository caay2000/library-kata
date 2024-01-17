package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.librarykata.hexagonal.context.application.loan.finish.FinishLoanCommand
import com.github.caay2000.librarykata.hexagonal.context.application.loan.finish.FinishLoanCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.finish.LoanFinisherError
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FinishLoanController(
    private val dateProvider: DateProvider,
    loanRepository: LoanRepository,
    bookRepository: BookRepository,
    accountRepository: AccountRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = FinishLoanCommandHandler(loanRepository, bookRepository, accountRepository)

    override suspend fun handle(call: ApplicationCall) {
        val bookId = UUID.fromString(call.parameters["bookId"])

        val datetime = dateProvider.dateTime()
        commandHandler.invoke(FinishLoanCommand(bookId = bookId, finishedAt = datetime))

        // TODO should it return loanId ?
        call.respond(HttpStatusCode.Accepted)
    }

    override suspend fun handleExceptions(
        call: ApplicationCall,
        e: Exception,
    ) {
        call.serverError {
            when (e) {
                is LoanFinisherError.LoanNotFound -> ServerResponse(HttpStatusCode.BadRequest, "LoanNotFound", e.message)
                else -> ServerResponse(HttpStatusCode.InternalServerError, "Unknown Error", e.message)
            }
        }
    }
}

package com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http

import com.github.caay2000.common.date.provider.DateProvider
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.application.finish.FinishLoanCommand
import com.github.caay2000.librarykata.hexagonal.context.loan.application.finish.FinishLoanCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.loan.application.finish.LoanFinisherError
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
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

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Loan")
            description = "Finish Loan"
            request {
                pathParameter<String>("id") {
                    description = "Book Id"
                    required = true
                    example = "00000000-0000-0000-0000-000000000000"
                }
            }

            response {
                HttpStatusCode.Accepted to { }
                errorResponses(
                    httpStatusCode = HttpStatusCode.BadRequest,
                    summary = "Error finding Loan",
                    responseExample("LoanNotFound", "Loan {loanId} not found"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

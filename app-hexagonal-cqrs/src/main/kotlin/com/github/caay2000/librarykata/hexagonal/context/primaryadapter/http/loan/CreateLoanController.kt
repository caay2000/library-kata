package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.librarykata.hexagonal.context.application.loan.create.CreateLoanCommand
import com.github.caay2000.librarykata.hexagonal.context.application.loan.create.CreateLoanCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.create.LoanCreatorError
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.LoanDocumentTransformer
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanRequestResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class CreateLoanController(
    private val idGenerator: IdGenerator,
    private val dateProvider: DateProvider,
    bookRepository: BookRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateLoanCommandHandler(bookRepository, accountRepository, loanRepository)
    private val loanQueryHandler = FindLoanByIdQueryHandler(loanRepository)
    private val transformer: Transformer<Loan, JsonApiDocument<LoanResource>> = LoanDocumentTransformer(accountRepository, bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<JsonApiRequestDocument<LoanRequestResource>>()

        val loanId = idGenerator.generate()
        val datetime = dateProvider.dateTime()
        commandHandler.invoke(request.toCreateLoanCommand(loanId, datetime))

        val loanQueryResponse = loanQueryHandler.invoke(FindLoanByIdQuery(LoanId(loanId)))
        call.respond(HttpStatusCode.Created, transformer.invoke(loanQueryResponse.loan))
    }

    override suspend fun handleExceptions(
        call: ApplicationCall,
        e: Exception,
    ) {
        call.serverError {
            when (e) {
                is LoanCreatorError.UserNotFound -> ServerResponse(HttpStatusCode.BadRequest, "UserNotFound", e.message)
                is LoanCreatorError.UserHasTooManyLoans -> ServerResponse(HttpStatusCode.BadRequest, "UserHasTooManyLoans", e.message)
                is LoanCreatorError.BookNotAvailable -> ServerResponse(HttpStatusCode.BadRequest, "BookNotAvailable", e.message)
                else -> ServerResponse(HttpStatusCode.InternalServerError, "Unknown Error", e.message)
            }
        }
    }

    private fun JsonApiRequestDocument<LoanRequestResource>.toCreateLoanCommand(
        loanId: String,
        datetime: LocalDateTime,
    ) = CreateLoanCommand(
        loanId = UUID.fromString(loanId),
        accountId = UUID.fromString(data.attributes.accountId),
        bookIsbn = data.attributes.bookIsbn,
        createdAt = datetime,
    )

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Loan")
            description = "Create Loan"
            request {
                body<JsonApiRequestDocument<LoanRequestResource>> {
                    mediaType(ContentType.JsonApi)
                    required = true
                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Loan Created"
                    body<JsonApiDocument<LoanResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.BadRequest,
                    summary = "Invalid request creating Account",
                    responseExample("UserNotFound", "book with isbn {bookIsbn} is not available"),
                    responseExample("UserHasTooManyLoans", "user {accountId} not found"),
                    responseExample("BookNotAvailable", "user {accountId} has too many loans"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

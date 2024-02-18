package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http

import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.date.provider.DateProvider
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.librarykata.eventdriven.context.loan.application.create.CreateLoanCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.application.create.CreateLoanCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.create.LoanCreatorError
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.find.FindLoanQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.LoanDocumentTransformer
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
    accountRepository: AccountRepository,
    bookRepository: BookRepository,
    loanRepository: LoanRepository,
    eventPublisher: DomainEventPublisher,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler: CommandHandler<CreateLoanCommand> = CreateLoanCommandHandler(accountRepository, bookRepository, loanRepository, eventPublisher)
    private val loanQueryHandler: QueryHandler<FindLoanQuery, FindLoanQueryResponse> = FindLoanQueryHandler(loanRepository)
    private val transformer: Transformer<Loan, JsonApiDocument<LoanResource>> = LoanDocumentTransformer()

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<JsonApiRequestDocument<LoanRequestResource>>()

        val loanId = idGenerator.generate()
        val datetime = dateProvider.dateTime()
        commandHandler.invoke(request.toCreateLoanCommand(loanId, datetime))

        val loanQueryResponse = loanQueryHandler.invoke(FindLoanQuery(LoanId(loanId)))
        call.respond(HttpStatusCode.Created, transformer.invoke(loanQueryResponse.loan))
    }

    override suspend fun handleExceptions(
        call: ApplicationCall,
        e: Exception,
    ) {
        call.serverError {
            when (e) {
                is LoanCreatorError.AccountNotFound -> ServerResponse(HttpStatusCode.BadRequest, "AccountNotFound", e.message)
                is LoanCreatorError.AccountHasTooManyLoans -> ServerResponse(HttpStatusCode.BadRequest, "AccountHasTooManyLoans", e.message)
                is LoanCreatorError.BookNotAvailable -> ServerResponse(HttpStatusCode.BadRequest, "BookNotAvailable", e.message)
                is LoanCreatorError.BookNotFound -> ServerResponse(HttpStatusCode.BadRequest, "BookNotFound", e.message)
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
                    responseExample("AccountNotFound", "Account {accountId} not found"),
                    responseExample("AccountHasTooManyLoans", "Account {accountId} has too many loans"),
                    responseExample("BookNotAvailable", "Account {accountId} has too many loans"),
                    responseExample("BookNotFound", "Book with isbn {bookIsbn} not found"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

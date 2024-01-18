package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.FindLoanByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.find.LoanFinderError
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.LoanDocumentTransformer
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindLoanController(
    accountRepository: AccountRepository,
    bookRepository: BookRepository,
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = FindLoanByIdQueryHandler(loanRepository)
    private val transformer: Transformer<Loan, JsonApiDocument<LoanResource>> = LoanDocumentTransformer(accountRepository, bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val loanId = UUID.fromString(call.parameters["loanId"]!!)
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResult = queryHandler.invoke(FindLoanByIdQuery(LoanId(loanId.toString())))

        call.respond(HttpStatusCode.OK, transformer.invoke(queryResult.loan, jsonApiParams.include))
    }

    override suspend fun handleExceptions(
        call: ApplicationCall,
        e: Exception,
    ) {
        call.serverError {
            when (e) {
                is LoanFinderError.LoanNotFoundError -> ServerResponse(HttpStatusCode.NotFound, "LoanNotFoundError", e.message)
                else -> ServerResponse(HttpStatusCode.InternalServerError, "Unknown Error", e.message)
            }
        }
    }

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Loan")
            description = "Find Loan"
            request {
                pathParameter<String>("id") {
                    description = "Loan Id"
                    required = true
                    example = "00000000-0000-0000-0000-000000000000"
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Loan Information"
                    body<JsonApiDocument<LoanResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.NotFound,
                    summary = "Error finding Loan",
                    responseExample("LoanNotFoundError", "Loan {loanId} not found"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

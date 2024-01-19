package com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.find.FindLoanHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.find.FindLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.find.FindLoanQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.find.LoanFinderError
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanDocumentTransformer
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
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler: QueryHandler<FindLoanQuery, FindLoanQueryResponse> = FindLoanHandler(loanRepository)
    private val transformer: Transformer<Loan, JsonApiDocument<LoanResource>> = LoanDocumentTransformer()

    override suspend fun handle(call: ApplicationCall) {
        val loanId = UUID.fromString(call.parameters["loanId"]!!)
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResult = queryHandler.invoke(FindLoanQuery(LoanId(loanId.toString())))

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

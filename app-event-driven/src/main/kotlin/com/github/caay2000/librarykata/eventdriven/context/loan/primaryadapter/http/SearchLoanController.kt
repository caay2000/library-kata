package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.common.query.ResourceQueryBus
import com.github.caay2000.librarykata.eventdriven.context.loan.application.search.SearchLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.application.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.application.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.LoanDocumentListTransformer
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging

class SearchLoanController(
    loanRepository: LoanRepository,
    resourceBus: ResourceQueryBus,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)
    private val transformer: Transformer<List<Loan>, JsonApiDocumentList<LoanResource>> = LoanDocumentListTransformer(resourceBus)

    override suspend fun handle(call: ApplicationCall) {
        val jsonApiParams = call.parameters.toMap().toJsonApiRequestParams()
        val queryResponse = queryHandler.invoke(SearchLoanQuery.AllLoan)
        val document = transformer.invoke(queryResponse.value, jsonApiParams.include)
        call.respond(HttpStatusCode.OK, document)
    }

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Loan")
            description = "Search All Loans"

            response {
                HttpStatusCode.OK to {
                    description = "Loans retrieved"
                    body<JsonApiDocumentList<LoanResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

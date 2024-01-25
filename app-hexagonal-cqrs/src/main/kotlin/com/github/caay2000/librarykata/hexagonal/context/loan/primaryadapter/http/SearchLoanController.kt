package com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.application.find.LoanFinderError
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.LoanDocumentListTransformer
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging

class SearchLoanController(
    accountRepository: AccountRepository,
    bookRepository: BookRepository,
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)
    private val transformer: Transformer<List<Loan>, JsonApiDocumentList<LoanResource>> = LoanDocumentListTransformer(accountRepository, bookRepository)

    override suspend fun handle(call: ApplicationCall) {
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResult = queryHandler.invoke(SearchLoanQuery.All)

        call.respond(HttpStatusCode.OK, transformer.invoke(queryResult.value, jsonApiParams.include))
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

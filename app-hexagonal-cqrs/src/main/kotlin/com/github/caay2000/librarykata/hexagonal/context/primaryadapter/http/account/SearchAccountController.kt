package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiRequestParams
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.application.account.search.SearchAccountQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.search.SearchAccountQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.AccountDocumentListTransformer
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging

class SearchAccountController(
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = SearchAccountQueryHandler(accountRepository)
    private val transformer: Transformer<List<Account>, JsonApiDocumentList<AccountResource>> = AccountDocumentListTransformer(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val jsonApiParams = call.parameters.toMap().toJsonApiRequestParams()
        val queryResponse = queryHandler.invoke(jsonApiParams.toQuery())
        val document = transformer.invoke(queryResponse.accounts, jsonApiParams.include)
        call.respond(HttpStatusCode.OK, document)
    }

    private fun JsonApiRequestParams.toQuery() =
        when {
            filter.containsKey("phoneNumber") -> SearchAccountQuery.SearchAccountByPhoneNumberQuery(filter["phoneNumber"]!!.first())
            filter.containsKey("email") -> SearchAccountQuery.SearchAccountByEmailQuery(filter["email"]!!.first())
            else -> SearchAccountQuery.SearchAllAccountQuery
        }

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Account")
            description = "Search All Accounts"
            request {
                queryParameter<String>("filter[phoneNumber]") {
                    description = "Filter by Account Phone Number (partial)"
                    required = false
                    example = "60012"
                }
                queryParameter<String>("filter[email]") {
                    description = "Filter by Account Email (partial)"
                    required = false
                    example = "@email"
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Accounts retrieved"
                    body<JsonApiDocumentList<AccountResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                    responseExample("UnknownError", "message with information about the error"),
                )
            }
        }
    }
}

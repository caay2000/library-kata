package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.RequestInclude
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.ServerResponse
import com.github.caay2000.common.jsonapi.documentation.errorResponses
import com.github.caay2000.common.jsonapi.documentation.responseExample
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.AccountFinderError
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.AccountToAccountDocumentTransformer
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import io.ktor.util.toMap
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindAccountController(accountRepository: AccountRepository, loanRepository: LoanRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val accountQueryHandler = FindAccountByIdQueryHandler(accountRepository)
    private val transformer: Transformer<Account, JsonApiDocument<AccountResource>> = AccountToAccountDocumentTransformer(loanRepository)

    internal enum class Included : RequestInclude { LOANS }

    override suspend fun handle(call: ApplicationCall) {
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!).toString())
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResult = accountQueryHandler.invoke(FindAccountByIdQuery(accountId))
        val responseDocument = transformer.invoke(queryResult.account, jsonApiParams.include)
        call.respond(HttpStatusCode.OK, responseDocument)
    }

    override suspend fun handleExceptions(call: ApplicationCall, e: Exception) {
        call.serverError {
            when (e) {
                is AccountFinderError.AccountNotFoundError -> ServerResponse(HttpStatusCode.NotFound, "AccountNotFoundError", e.message)
                else -> ServerResponse(HttpStatusCode.InternalServerError, "Unknown Error", e.message)
            }
        }
    }

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Account")
            description = "Find Account"
            request {
                pathParameter<String>("id") {
                    description = "Account Id"
                    required = true
                    example = "00000000-0000-0000-0000-000000000000"
                }
            }

            response {
                HttpStatusCode.OK to {
                    description = "Account Information"
                    body<JsonApiDocument<AccountResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.NotFound,
                    summary = "Error finding Account",
                    responseExample("AccountNotFoundError", "Account {accountId} not found"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                    responseExample("UnknownError", "message with information about the error"),
                )
            }
        }
    }
}

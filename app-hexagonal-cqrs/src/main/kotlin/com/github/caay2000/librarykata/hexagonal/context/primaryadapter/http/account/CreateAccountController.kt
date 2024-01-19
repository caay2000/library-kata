package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.common.cqrs.QueryHandler
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
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.AccountCreatorError
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.CreateAccountCommand
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.CreateAccountCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.AccountDocumentTransformer
import com.github.caay2000.librarykata.jsonapi.context.account.AccountRequestResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import io.github.smiley4.ktorswaggerui.dsl.OpenApiRoute
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class CreateAccountController(
    private val idGenerator: IdGenerator,
    private val dateProvider: DateProvider,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : Controller {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler: CommandHandler<CreateAccountCommand> = CreateAccountCommandHandler(accountRepository)
    private val queryHandler: QueryHandler<FindAccountQuery, FindAccountQueryResponse> = FindAccountQueryHandler(accountRepository)
    private val transformer: Transformer<Account, JsonApiDocument<AccountResource>> = AccountDocumentTransformer(loanRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<JsonApiRequestDocument<AccountRequestResource>>()
        val accountId = idGenerator.generate()
        val registerDate = dateProvider.dateTime()
        commandHandler.invoke(request.toCommand(accountId, registerDate))

        val queryResponse = queryHandler.invoke(FindAccountQuery(AccountId(accountId)))
        call.respond(HttpStatusCode.Created, transformer.invoke(queryResponse.account))
    }

    override suspend fun handleExceptions(
        call: ApplicationCall,
        e: Exception,
    ) {
        call.serverError {
            when (e) {
                is AccountCreatorError.IdentityNumberAlreadyExists -> ServerResponse(HttpStatusCode.BadRequest, "IdentityNumberAlreadyExists", e.message)
                is AccountCreatorError.EmailAlreadyExists -> ServerResponse(HttpStatusCode.BadRequest, "EmailAlreadyExists", e.message)
                is AccountCreatorError.PhoneAlreadyExists -> ServerResponse(HttpStatusCode.BadRequest, "PhoneAlreadyExists", e.message)
                else -> ServerResponse(HttpStatusCode.InternalServerError, "Unknown Error", e.message)
            }
        }
    }

    private fun JsonApiRequestDocument<AccountRequestResource>.toCommand(
        accountId: String,
        registerDate: LocalDateTime,
    ): CreateAccountCommand =
        CreateAccountCommand(
            accountId = UUID.fromString(accountId),
            identityNumber = data.attributes.identityNumber,
            email = data.attributes.email,
            phoneNumber = data.attributes.phoneNumber,
            phonePrefix = data.attributes.phonePrefix,
            name = data.attributes.name,
            surname = data.attributes.surname,
            birthdate = data.attributes.birthdate,
            registerDate = registerDate,
        )

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            tags = listOf("Account")
            description = "Create Account"
            request {
                body<JsonApiRequestDocument<AccountRequestResource>> {
                    mediaType(ContentType.JsonApi)
                    required = true
                }
            }
            response {
                HttpStatusCode.Created to {
                    description = "Account Created"
                    body<JsonApiDocument<AccountResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                errorResponses(
                    httpStatusCode = HttpStatusCode.BadRequest,
                    summary = "Invalid request creating Account",
                    responseExample("IdentityNumberAlreadyExists", "An account with identity number {identityNumber} already exists"),
                    responseExample("EmailAlreadyExists", "An account with email {email} already exists"),
                    responseExample("PhoneAlreadyExists", "An account with phone {phonePrefix} {phoneNumber} already exists"),
                    responseExample("InvalidJsonApiException", "Invalid type for AccountResource: {type}"),
                )
                errorResponses(
                    httpStatusCode = HttpStatusCode.InternalServerError,
                    summary = "Something unexpected happened",
                )
            }
        }
    }
}

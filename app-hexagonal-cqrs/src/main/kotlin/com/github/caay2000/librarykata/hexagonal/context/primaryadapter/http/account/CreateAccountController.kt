package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.http.ContentType
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.context.account.AccountRequestResource
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.CreateAccountCommand
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.CreateAccountCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.CreateAccountToAccountDocumentTransformer
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
) : Controller {

    companion object {
        val documentation: OpenApiRoute.() -> Unit = {
            description = "Create Account"
            request {
                body<JsonApiRequestDocument<AccountRequestResource>> {
                    mediaType(ContentType.JsonApi)
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "Successful Request"
                    body<JsonApiDocument<AccountResource>> {
                        mediaType(ContentType.JsonApi)
                    }
                }
                HttpStatusCode.InternalServerError to {
                    description = "Something unexpected happened"
                }

                /*
                    class Unknown(error: Throwable) : AccountCreatorError(error)
                    class AccountNotFound : AccountCreatorError("account not foung")
                    class IdentityNumberAlreadyExists(identityNumber: IdentityNumber) :
                        AccountCreatorError("an account with identity number ${identityNumber.value} already exists")

                    class EmailAlreadyExists(email: Email) : AccountCreatorError("an account with email ${email.value} already exists")
                    class PhoneAlreadyExists(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber) :
                        AccountCreatorError("an account with phone ${phonePrefix.value} ${phoneNumber.value} already exists")
                 */
            }
        }
    }

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateAccountCommandHandler(accountRepository)
    private val queryHandler = FindAccountByIdQueryHandler(accountRepository)

    private val transformer: Transformer<Account, JsonApiDocument<AccountResource>> = CreateAccountToAccountDocumentTransformer()

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<JsonApiRequestDocument<AccountRequestResource>>()
        val accountId = idGenerator.generate()
        val registerDate = dateProvider.dateTime()
        commandHandler.invoke(request.toCommand(accountId, registerDate))

        val queryResult = queryHandler.invoke(FindAccountByIdQuery(AccountId(accountId)))
        call.respond(HttpStatusCode.Created, transformer.invoke(queryResult.account))
    }

    private fun JsonApiRequestDocument<AccountRequestResource>.toCommand(accountId: String, registerDate: LocalDateTime): CreateAccountCommand =
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
}

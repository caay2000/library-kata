package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.CreateAccountCommand
import com.github.caay2000.librarykata.hexagonal.context.application.account.create.CreateAccountCommandHandler
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountRequestDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toAccountDocument
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

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateAccountCommandHandler(accountRepository)
    private val queryHandler = FindAccountByIdQueryHandler(accountRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<AccountRequestDocument>()
        val accountId = idGenerator.generate()
        val registerDate = dateProvider.dateTime()
        commandHandler.invoke(request.toCommand(accountId, registerDate))

        val queryResult = queryHandler.invoke(FindAccountByIdQuery(AccountId(accountId)))
        call.respond(HttpStatusCode.Created, queryResult.account.toAccountDocument())
    }

    private fun AccountRequestDocument.toCommand(accountId: String, registerDate: LocalDateTime): CreateAccountCommand =
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

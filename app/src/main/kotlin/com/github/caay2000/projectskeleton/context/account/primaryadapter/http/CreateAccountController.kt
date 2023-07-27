package com.github.caay2000.projectskeleton.context.account.primaryadapter.http

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.application.create.CreateAccountCommand
import com.github.caay2000.projectskeleton.context.account.application.create.CreateAccountCommandHandler
import com.github.caay2000.projectskeleton.context.account.application.find.FindAccountByIdQuery
import com.github.caay2000.projectskeleton.context.account.application.find.FindAccountByIdQueryHandler
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.serialization.CreateAccountRequestDocument
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.serialization.toAccountDetailsDocument
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
    eventPublisher: DomainEventPublisher,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateAccountCommandHandler(accountRepository, eventPublisher)
    private val queryHandler = FindAccountByIdQueryHandler(accountRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<CreateAccountRequestDocument>()
        val accountId = UUID.fromString(idGenerator.generate())
        val registerDate = dateProvider.dateTime()
        commandHandler.invoke(request.toCommand(accountId, registerDate))

        val queryResult = queryHandler.handle(FindAccountByIdQuery(AccountId(accountId)))
        call.respond(HttpStatusCode.Created, queryResult.account.toAccountDetailsDocument())
    }

    private fun CreateAccountRequestDocument.toCommand(accountId: UUID, registerDate: LocalDateTime): CreateAccountCommand =
        CreateAccountCommand(
            accountId = accountId,
            identityNumber = identityNumber,
            email = email,
            phoneNumber = phoneNumber,
            phonePrefix = phonePrefix,
            name = name,
            surname = surname,
            birthdate = birthdate,
            registerDate = registerDate,
        )
}

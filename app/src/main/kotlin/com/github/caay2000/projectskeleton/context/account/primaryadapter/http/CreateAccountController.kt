package com.github.caay2000.projectskeleton.context.account.primaryadapter.http

import com.github.caay2000.common.dateprovider.DateProvider
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.application.create.CreateAccountCommand
import com.github.caay2000.projectskeleton.context.account.application.create.CreateAccountCommandHandler
import com.github.caay2000.projectskeleton.context.account.application.find.FindAccountByIdQuery
import com.github.caay2000.projectskeleton.context.account.application.find.FindAccountByIdQueryHandler
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime

class CreateAccountController(
    private val accountNumberGenerator: IdGenerator,
    private val dateProvider: DateProvider,
    accountRepository: AccountRepository,
    eventPublisher: DomainEventPublisher,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateAccountCommandHandler(accountRepository, eventPublisher)
    private val queryHandler = FindAccountByIdQueryHandler(accountRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<CreateAccountRequestDocument>()
        val accountNumber = accountNumberGenerator.generate()
        val registerDate = dateProvider.dateTime()
        commandHandler.invoke(request.toCommand(accountNumber, registerDate))

        val queryResult = queryHandler.handle(FindAccountByIdQuery(accountNumber))
        call.respond(HttpStatusCode.Created, queryResult.account.toAccountDetailsDocument())
    }

    private fun CreateAccountRequestDocument.toCommand(accountNumber: String, registerDate: LocalDateTime): CreateAccountCommand =
        CreateAccountCommand(
            accountNumber = accountNumber,
            email = email,
            phoneNumber = phoneNumber,
            phonePrefix = phonePrefix,
            name = name,
            surname = surname,
            birthDate = birthDate,
            registerDate = registerDate,
        )
}

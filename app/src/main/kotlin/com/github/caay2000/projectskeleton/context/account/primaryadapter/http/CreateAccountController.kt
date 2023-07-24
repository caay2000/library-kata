package com.github.caay2000.projectskeleton.context.account.primaryadapter.http

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
import java.util.UUID

class CreateAccountController(
    private val idGenerator: IdGenerator,
    accountRepository: AccountRepository,
    eventPublisher: DomainEventPublisher,
) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateAccountCommandHandler(accountRepository, eventPublisher)
    private val queryHandler = FindAccountByIdQueryHandler(accountRepository)

    override suspend fun handle(call: ApplicationCall) {
        val request = call.receive<CreateAccountRequestDocument>()
        val accountId = UUID.fromString(idGenerator.generate())
        commandHandler.invoke(request.toCommand(accountId))

        val queryResult = queryHandler.handle(FindAccountByIdQuery(accountId))
        call.respond(HttpStatusCode.Created, queryResult.account.toAccountDetailsDocument())
    }

    private fun CreateAccountRequestDocument.toCommand(id: UUID): CreateAccountCommand =
        CreateAccountCommand(accountId = id, email = email, name = name)
}

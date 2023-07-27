package com.github.caay2000.projectskeleton.context.account.primaryadapter.http

import com.github.caay2000.common.http.Controller
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.application.find.FindAccountByIdQuery
import com.github.caay2000.projectskeleton.context.account.application.find.FindAccountByIdQueryHandler
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.primaryadapter.http.serialization.toAccountDetailsDocument
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class FindAccountController(accountRepository: AccountRepository) : Controller {

    override val logger: KLogger = KotlinLogging.logger {}

    private val queryHandler = FindAccountByIdQueryHandler(accountRepository)

    override suspend fun handle(call: ApplicationCall) {
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!))

        val queryResult = queryHandler.handle(FindAccountByIdQuery(accountId))
        call.respond(HttpStatusCode.OK, queryResult.account.toAccountDetailsDocument())
    }
}

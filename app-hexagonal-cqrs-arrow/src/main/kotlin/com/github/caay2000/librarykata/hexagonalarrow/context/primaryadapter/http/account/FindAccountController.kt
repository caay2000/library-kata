package com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.account

import com.github.caay2000.common.http.Controller
import com.github.caay2000.librarykata.hexagonalarrow.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonalarrow.context.application.account.find.FindAccountByIdQuery
import com.github.caay2000.librarykata.hexagonalarrow.context.application.account.find.FindAccountByIdQueryHandler
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization.toAccountDetailsDocument
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
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!).toString())

        val queryResult = queryHandler.invoke(FindAccountByIdQuery(accountId))
        call.respond(HttpStatusCode.OK, queryResult.account.toAccountDetailsDocument())
    }
}

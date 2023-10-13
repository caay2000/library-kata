package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account

import com.github.caay2000.common.http.Controller
import com.github.caay2000.common.http.RequestInclude
import com.github.caay2000.common.http.Transfomer
import com.github.caay2000.common.jsonapi.toJsonApiRequestParams
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.account.find.FindAccountByIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.AccountToAccountDocumentTransformer
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountDocument
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
    private val transformer: Transfomer<Account, AccountDocument> = AccountToAccountDocumentTransformer(loanRepository)

    internal enum class Included : RequestInclude { LOANS }

    override suspend fun handle(call: ApplicationCall) {
        val accountId = AccountId(UUID.fromString(call.parameters["id"]!!).toString())
        val jsonApiParams = call.request.queryParameters.toMap().toJsonApiRequestParams()

        val queryResult = accountQueryHandler.invoke(FindAccountByIdQuery(accountId))
        val responseDocument = transformer.invoke(queryResult.account, jsonApiParams.include)
        logger.trace { responseDocument }
        call.respond(HttpStatusCode.OK, responseDocument)
    }
}

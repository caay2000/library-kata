package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryHandler
import com.github.caay2000.common.query.ResourceQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.eventdriven.context.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import mu.KLogger
import mu.KotlinLogging

class AccountResourceQueryBusHandler(accountRepository: AccountRepository, loanRepository: LoanRepository) : ResourceQueryHandler {
    private val accountQueryHandler = FindAccountQueryHandler(accountRepository)
    private val accountResourceTransformer = AccountResourceTransformer(loanRepository)

    override val logger: KLogger = KotlinLogging.logger {}

    override val type: String = AccountResource.TYPE

    override fun handle(query: ResourceQuery): ResourceQueryResponse {
        val account = retrieveAccount(query)
        return ResourceQueryResponse(accountResourceTransformer.invoke(account))
    }

    private fun retrieveAccount(query: ResourceQuery): Account = accountQueryHandler.invoke(FindAccountQuery(AccountId(query.identifier))).account
}

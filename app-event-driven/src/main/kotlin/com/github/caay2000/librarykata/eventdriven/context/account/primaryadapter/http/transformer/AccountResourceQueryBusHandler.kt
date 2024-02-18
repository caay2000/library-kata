package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.querybus.Query
import com.github.caay2000.common.querybus.QueryBusHandler
import com.github.caay2000.common.querybus.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.eventdriven.context.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class AccountResourceQueryBusHandler(accountRepository: AccountRepository, loanRepository: LoanRepository) : QueryBusHandler<AccountResourceQuery, AccountResourceQueryResponse> {
    private val accountQueryHandler = FindAccountQueryHandler(accountRepository)
    private val accountResourceTransformer = AccountResourceTransformer(loanRepository)

    override val logger: KLogger = KotlinLogging.logger {}

    override fun handle(query: AccountResourceQuery): AccountResourceQueryResponse {
        val account = retrieveAccount(query)
        return AccountResourceQueryResponse(accountResourceTransformer.invoke(account))
    }

    private fun retrieveAccount(query: AccountResourceQuery): Account =
        when (query) {
            is AccountResourceQuery.ByIdentifier -> accountQueryHandler.invoke(FindAccountQuery(AccountId(query.identifier))).account
            is AccountResourceQuery.ByValue -> query.value
        }
}

sealed class AccountResourceQuery : Query {
    data class ByIdentifier(val identifier: String) : AccountResourceQuery()

    data class ByValue(val value: Account) : AccountResourceQuery()
}

data class AccountResourceQueryResponse(val resource: JsonApiResource) : QueryResponse

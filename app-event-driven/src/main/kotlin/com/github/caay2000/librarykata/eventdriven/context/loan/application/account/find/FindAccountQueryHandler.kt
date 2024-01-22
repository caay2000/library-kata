package com.github.caay2000.librarykata.eventdriven.context.loan.application.account.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import mu.KLogger
import mu.KotlinLogging

class FindAccountQueryHandler(accountRepository: AccountRepository) :
    QueryHandler<FindAccountQuery, FindAccountQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = AccountFinder(accountRepository)

    override fun handle(query: FindAccountQuery): FindAccountQueryResponse =
        finder.invoke(query.accountId)
            .map { FindAccountQueryResponse(it) }
            .getOrThrow()
}

data class FindAccountQuery(val accountId: AccountId) : Query

data class FindAccountQueryResponse(val account: Account) : QueryResponse

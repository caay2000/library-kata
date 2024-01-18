package com.github.caay2000.librarykata.hexagonal.context.application.account.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import mu.KLogger
import mu.KotlinLogging

class FindAccountQueryHandler(accountRepository: AccountRepository) : QueryHandler<FindAccountQuery, FindAccountQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = AccountFinder(accountRepository)

    override fun handle(query: FindAccountQuery): FindAccountQueryResponse =
        finder.invoke(query.accountId)
            .map { FindAccountQueryResponse(it) }
            .getOrThrow()
}

data class FindAccountQuery(val accountId: AccountId) : Query

data class FindAccountQueryResponse(val account: Account) : QueryResponse

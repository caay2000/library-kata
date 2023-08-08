package com.github.caay2000.librarykata.context.account.application.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.context.account.application.AccountRepository
import com.github.caay2000.librarykata.context.account.domain.Account
import com.github.caay2000.librarykata.context.account.domain.AccountId
import mu.KLogger
import mu.KotlinLogging

class FindAccountByIdQueryHandler(accountRepository: AccountRepository) : QueryHandler<FindAccountByIdQuery, FindAccountByIdQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val finder = AccountFinder(accountRepository)

    override fun handle(query: FindAccountByIdQuery): FindAccountByIdQueryResponse =
        finder.invoke(query.accountId)
            .map { FindAccountByIdQueryResponse(it) }
            .getOrThrow()
}

data class FindAccountByIdQuery(val accountId: AccountId) : Query

data class FindAccountByIdQueryResponse(val account: Account) : QueryResponse

package com.github.caay2000.projectskeleton.context.account.application.find

import com.github.caay2000.archkata.common.cqrs.Query
import com.github.caay2000.archkata.common.cqrs.QueryHandler
import com.github.caay2000.archkata.common.cqrs.QueryResponse
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountNumber

class FindAccountByIdQueryHandler(accountRepository: AccountRepository) : QueryHandler<FindAccountByIdQuery, FindAccountByIdQueryResult> {

    private val finder = AccountFinder(accountRepository)

    override fun handle(query: FindAccountByIdQuery): FindAccountByIdQueryResult =
        finder.invoke(AccountNumber(query.accountNumber))
            .map { FindAccountByIdQueryResult(it) }
            .getOrThrow()
}

data class FindAccountByIdQuery(val accountNumber: String) : Query

data class FindAccountByIdQueryResult(val account: Account) : QueryResponse

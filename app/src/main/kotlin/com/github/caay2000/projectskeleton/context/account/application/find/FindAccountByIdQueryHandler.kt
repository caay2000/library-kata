package com.github.caay2000.projectskeleton.context.account.application.find

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.projectskeleton.common.cqrs.Query
import com.github.caay2000.projectskeleton.common.cqrs.QueryHandler
import com.github.caay2000.projectskeleton.common.cqrs.QueryResponse
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountId

class FindAccountByIdQueryHandler(accountRepository: AccountRepository) : QueryHandler<FindAccountByIdQuery, FindAccountByIdQueryResponse> {

    private val finder = AccountFinder(accountRepository)

    override fun handle(query: FindAccountByIdQuery): FindAccountByIdQueryResponse =
        finder.invoke(query.accountId)
            .map { FindAccountByIdQueryResponse(it) }
            .getOrThrow()
}

data class FindAccountByIdQuery(val accountId: AccountId) : Query

data class FindAccountByIdQueryResponse(val account: Account) : QueryResponse

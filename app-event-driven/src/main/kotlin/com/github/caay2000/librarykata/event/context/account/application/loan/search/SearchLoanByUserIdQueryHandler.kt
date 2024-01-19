package com.github.caay2000.librarykata.event.context.account.application.loan.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.event.context.account.application.LoanRepository
import com.github.caay2000.librarykata.event.context.account.domain.AccountId
import com.github.caay2000.librarykata.event.context.account.domain.Loan
import mu.KLogger
import mu.KotlinLogging

class SearchLoanByUserIdQueryHandler(
    loanRepository: LoanRepository,
) : QueryHandler<SearchLoanByAccountIdQuery, SearchLoanByAccountIdQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = LoanSearcher(loanRepository)

    override fun handle(query: SearchLoanByAccountIdQuery): SearchLoanByAccountIdQueryResponse = SearchLoanByAccountIdQueryResponse(searcher.invoke(query.accountId))
}

data class SearchLoanByAccountIdQuery(val accountId: AccountId) : Query

data class SearchLoanByAccountIdQueryResponse(val value: List<Loan>) : QueryResponse

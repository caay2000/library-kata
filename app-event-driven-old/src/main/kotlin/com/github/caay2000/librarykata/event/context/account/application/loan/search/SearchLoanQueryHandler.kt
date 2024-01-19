package com.github.caay2000.librarykata.event.context.account.application.loan.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.event.context.account.application.LoanRepository
import com.github.caay2000.librarykata.event.context.account.domain.AccountId
import com.github.caay2000.librarykata.event.context.account.domain.Loan
import mu.KLogger
import mu.KotlinLogging

class SearchLoanQueryHandler(
    loanRepository: LoanRepository,
) : QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = LoanSearcher(loanRepository)

    override fun handle(query: SearchLoanQuery): SearchLoanQueryResponse = SearchLoanQueryResponse(searcher.invoke(AccountId(query.accountId)))
}

data class SearchLoanQuery(val accountId: String) : Query

data class SearchLoanQueryResponse(val value: List<Loan>) : QueryResponse

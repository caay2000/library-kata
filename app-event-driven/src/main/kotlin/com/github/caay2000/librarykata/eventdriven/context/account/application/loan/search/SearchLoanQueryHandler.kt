package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class SearchLoanQueryHandler(loanRepository: LoanRepository) : QueryHandler<SearchLoanByAccountIdQuery, SearchLoanQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = LoanSearcher(loanRepository)

    override fun handle(query: SearchLoanByAccountIdQuery): SearchLoanQueryResponse = SearchLoanQueryResponse(searcher.invoke(query.accountId))
}

data class SearchLoanByAccountIdQuery(val accountId: AccountId) : Query

data class SearchLoanQueryResponse(val loans: List<Loan>) : QueryResponse

package com.github.caay2000.librarykata.hexagonal.context.application.loan.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import mu.KLogger
import mu.KotlinLogging

class SearchLoansByAccountIdQueryHandler(
    loanRepository: LoanRepository,
) : QueryHandler<SearchLoanByAccountIdQuery, SearchLoanByAccountIdQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = LoanSearcher(loanRepository)

    override fun handle(query: SearchLoanByAccountIdQuery): SearchLoanByAccountIdQueryResponse =
        searcher.invoke(query.accountId)
            .map { loans -> SearchLoanByAccountIdQueryResponse(loans) }
            .getOrThrow()
}

data class SearchLoanByAccountIdQuery(val accountId: AccountId) : Query

data class SearchLoanByAccountIdQueryResponse(val value: List<Loan>) : QueryResponse

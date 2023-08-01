package com.github.caay2000.librarykata.context.book.application.loan.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.context.book.application.LoanRepository
import com.github.caay2000.librarykata.context.book.domain.Loan
import com.github.caay2000.librarykata.context.book.domain.UserId

class SearchLoanByUserIdQueryHandler(
    loanRepository: LoanRepository,
) : QueryHandler<SearchLoanByUserIdQuery, SearchLoanByUserIdQueryResponse> {

    private val searcher = LoanSearcher(loanRepository)

    override fun handle(query: SearchLoanByUserIdQuery): SearchLoanByUserIdQueryResponse =
        searcher.invoke(query.id)
            .map { loans -> SearchLoanByUserIdQueryResponse(loans) }
            .getOrThrow()
}

data class SearchLoanByUserIdQuery(val id: UserId) : Query

data class SearchLoanByUserIdQueryResponse(val value: List<Loan>) : QueryResponse

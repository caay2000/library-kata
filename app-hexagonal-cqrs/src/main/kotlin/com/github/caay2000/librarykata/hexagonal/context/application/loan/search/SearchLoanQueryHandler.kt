package com.github.caay2000.librarykata.hexagonal.context.application.loan.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.SearchLoanCriteria
import mu.KLogger
import mu.KotlinLogging

class SearchLoanQueryHandler(
    loanRepository: LoanRepository,
) : QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = LoanSearcher(loanRepository)

    override fun handle(query: SearchLoanQuery): SearchLoanQueryResponse = SearchLoanQueryResponse(searcher.invoke(query.toCriteria()))

    private fun SearchLoanQuery.toCriteria() =
        when (this) {
            is SearchLoanQuery.SearchLoanByAccountIdQuery -> SearchLoanCriteria.ByAccountId(AccountId(accountId))
            is SearchLoanQuery.SearchLoanByBookIdQuery -> SearchLoanCriteria.ByBookId(BookId(bookId))
            is SearchLoanQuery.SearchLoanByBookIsbnQuery -> SearchLoanCriteria.ByBookIsbn(BookIsbn(bookIsbn))
        }
}

sealed class SearchLoanQuery : Query {
    data class SearchLoanByAccountIdQuery(val accountId: String) : SearchLoanQuery()

    data class SearchLoanByBookIdQuery(val bookId: String) : SearchLoanQuery()

    data class SearchLoanByBookIsbnQuery(val bookIsbn: String) : SearchLoanQuery()
}

data class SearchLoanQueryResponse(val value: List<Loan>) : QueryResponse

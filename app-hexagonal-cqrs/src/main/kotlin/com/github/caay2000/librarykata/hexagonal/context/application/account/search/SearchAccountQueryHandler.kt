package com.github.caay2000.librarykata.hexagonal.context.application.account.search

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.SearchAccountCriteria
import mu.KLogger
import mu.KotlinLogging

class SearchAccountQueryHandler(accountRepository: AccountRepository) : QueryHandler<SearchAccountQuery, SearchAccountQueryResponse> {

    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = AccountSearcher(accountRepository)

    override fun handle(query: SearchAccountQuery): SearchAccountQueryResponse =
        query.toCriteria().let { criteria ->
            searcher.invoke(criteria)
                .map { books -> SearchAccountQueryResponse(books) }
                .getOrThrow()
        }

    private fun SearchAccountQuery.toCriteria() =
        when (this) {
            is SearchAccountQuery.SearchAllAccountQuery -> SearchAccountCriteria.All
            is SearchAccountQuery.SearchAccountByPhoneNumberQuery -> SearchAccountCriteria.ByPhoneNumber(phoneNumber)
            is SearchAccountQuery.SearchAccountByEmailQuery -> SearchAccountCriteria.ByEmail(email)
        }
}

sealed class SearchAccountQuery : Query {
    data object SearchAllAccountQuery : SearchAccountQuery()
    data class SearchAccountByPhoneNumberQuery(val phoneNumber: String) : SearchAccountQuery()
    data class SearchAccountByEmailQuery(val email: String) : SearchAccountQuery()
}

data class SearchAccountQueryResponse(val values: List<Account>) : QueryResponse

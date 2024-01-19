package com.github.caay2000.librarykata.hexagonal.context.application.account.search

import com.github.caay2000.common.cqrs.Query
import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.cqrs.QueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.SearchAccountCriteria
import mu.KLogger
import mu.KotlinLogging

class SearchAccountQueryHandler(accountRepository: AccountRepository) : QueryHandler<SearchAccountQuery, SearchAccountQueryResponse> {
    override val logger: KLogger = KotlinLogging.logger {}

    private val searcher = AccountSearcher(accountRepository)

    override fun handle(query: SearchAccountQuery): SearchAccountQueryResponse = SearchAccountQueryResponse(searcher.invoke(query.toCriteria()))

    private fun SearchAccountQuery.toCriteria() =
        when (this) {
            is SearchAccountQuery.SearchAllAccountQuery -> SearchAccountCriteria.All
            is SearchAccountQuery.SearchAccountByPhoneNumberQuery -> SearchAccountCriteria.ByPhoneNumber(PhoneNumber(phoneNumber))
            is SearchAccountQuery.SearchAccountByEmailQuery -> SearchAccountCriteria.ByEmail(Email(email))
        }
}

sealed class SearchAccountQuery : Query {
    data object SearchAllAccountQuery : SearchAccountQuery()

    data class SearchAccountByPhoneNumberQuery(val phoneNumber: String) : SearchAccountQuery()

    data class SearchAccountByEmailQuery(val email: String) : SearchAccountQuery()
}

data class SearchAccountQueryResponse(val accounts: List<Account>) : QueryResponse

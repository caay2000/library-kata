package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

class AccountListToAccountDocumentListTransformer(loanRepository: LoanRepository) : Transformer<List<Account>, JsonApiListDocument<AccountResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Account>,
        includes: List<String>,
    ): JsonApiListDocument<AccountResource> =
        JsonApiListDocument(
            data =
                value.map {
                    val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByAccountIdQuery(it.id.value)).value
                    it.toJsonApiDocumentAccountResource(loans)
                },
            meta = JsonApiMeta(total = value.size),
        )
}

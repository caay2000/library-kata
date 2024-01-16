package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentIncludedResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

class AccountToAccountDocumentTransformer(loanRepository: LoanRepository) : Transformer<Account, JsonApiDocument<AccountResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Account,
        includes: List<String>,
    ): JsonApiDocument<AccountResource> {
        // TODO When the Account is brand new, this query is not needed, as it won't have any relationship
        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByAccountIdQuery(value.id.value)).value
        return JsonApiDocument(
            data = value.toJsonApiDocumentAccountResource(loans),
            included =
                includes.shouldProcess(FindAccountController.Included.LOANS) {
                    loans.toJsonApiDocumentIncludedResource()
                },
        )
    }
}

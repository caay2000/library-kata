package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQuery
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoanByAccountIdQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.application.loan.search.SearchLoansByAccountIdQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.FindAccountController
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentIncludedResource
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentResource

class AccountToJsonApiDocumentTransformer(loanRepository: LoanRepository) : Transformer<Account, JsonApiDocument<AccountResource>> {

    private val loanQueryHandler: QueryHandler<SearchLoanByAccountIdQuery, SearchLoanByAccountIdQueryResponse> = SearchLoansByAccountIdQueryHandler(loanRepository)

    override fun invoke(value: Account, includes: List<String>): JsonApiDocument<AccountResource> {
        val loans = loanQueryHandler.invoke(SearchLoanByAccountIdQuery(value.id)).value
        return JsonApiDocument(
            data = value.toJsonApiDocumentResource(loans),
            included = includes.shouldProcess(FindAccountController.Included.LOANS) {
                loans.toJsonApiDocumentIncludedResource()
            },
        )
    }
}

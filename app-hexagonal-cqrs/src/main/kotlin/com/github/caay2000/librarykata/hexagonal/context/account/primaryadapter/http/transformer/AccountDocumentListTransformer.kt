package com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQuery
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.hexagonal.context.loan.application.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.toJsonApiLoanResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer

class AccountDocumentListTransformer(loanRepository: LoanRepository) : Transformer<List<Account>, JsonApiDocumentList<AccountResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Account>,
        include: List<String>,
    ): JsonApiDocumentList<AccountResource> {
        val loans = value.flatMap { loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByAccountIdAndNotFinishedQuery(it.id.value)).value }.toSet()
        return value.toJsonApiAccountDocumentList(loans, include)
    }
}

fun List<Account>.toJsonApiAccountDocumentList(
    loans: Collection<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocumentList(
    data = map { it.toJsonApiAccountResource(loans) },
    included =
        if (include.shouldProcess(LoanResource.TYPE))
            IncludeTransformer.invoke(
                loans.map { loan ->
                    loan.toJsonApiLoanResource(
                        account = this.firstOrNull { it.id.value == loan.accountId.value },
                    )
                },
            )
        else
            null,
    meta = JsonApiMeta(total = size),
)

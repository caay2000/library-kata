package com.github.caay2000.librarykata.event.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.event.context.account.application.LoanRepository
import com.github.caay2000.librarykata.event.context.account.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.event.context.account.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.event.context.account.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.event.context.account.domain.Account
import com.github.caay2000.librarykata.event.context.account.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

class AccountDocumentListTransformer(loanRepository: LoanRepository) : Transformer<List<Account>, JsonApiDocumentList<AccountResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Account>,
        include: List<String>,
    ): JsonApiDocumentList<AccountResource> {
        val loans = value.flatMap { loanQueryHandler.invoke(SearchLoanQuery(it.id.value)).value }.toSet()
        return value.toJsonApiAccountDocumentList(loans, include)
    }
}

fun List<Account>.toJsonApiAccountDocumentList(
    loans: Collection<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocumentList(
    data = map { it.toJsonApiAccountResource(loans) },
    included = null,
//    included = if (include.shouldProcess(LoanResource.TYPE)) LoanIncludeTransformer().invoke(loans) else null,
    meta = JsonApiMeta(total = size),
)

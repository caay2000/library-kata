package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.toJsonApiLoanResource
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer

class AccountDocumentListTransformer : Transformer<List<Account>, JsonApiDocumentList<AccountResource>> {
//    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: List<Account>,
        include: List<String>,
    ): JsonApiDocumentList<AccountResource> {
//        val loans = value.flatMap { loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByAccountIdQuery(it.id.value)).value }.toSet()
        return value.toJsonApiAccountDocumentList(emptyList(), include)
    }
}

fun List<Account>.toJsonApiAccountDocumentList(
    loans: Collection<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocumentList(
    data = map { it.toJsonApiAccountResource(emptyList()) },
    included = if (include.shouldProcess(LoanResource.TYPE)) IncludeTransformer.invoke(loans.map { it.toJsonApiLoanResource() }) else null,
    meta = JsonApiMeta(total = size),
)

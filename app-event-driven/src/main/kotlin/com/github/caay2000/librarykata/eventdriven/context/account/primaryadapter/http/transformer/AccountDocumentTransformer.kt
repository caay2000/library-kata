package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanIncludeTransformer
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.primaryadapter.http.serialization.LoanRelationshipTransformer
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class AccountDocumentTransformer : Transformer<Account, JsonApiDocument<AccountResource>> {
//    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Account,
        include: List<String>,
    ): JsonApiDocument<AccountResource> {
        // TODO When the Account is brand new, this query is not needed, as it won't have any relationship
//        val loans = loanQueryHandler.invoke(SearchLoanQuery.SearchLoanByAccountIdQuery(value.id.value)).value
        return value.toJsonApiAccountDocument(emptyList(), include)
    }
}

fun Account.toJsonApiAccountDocument(
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toJsonApiAccountResource(loans),
    included = if (include.shouldProcess(LoanResource.TYPE)) LoanIncludeTransformer().invoke(loans) else null,
)

internal fun Account.toJsonApiAccountResource(loans: Collection<Loan> = emptyList()) =
    AccountResource(
        id = id.value,
        type = AccountResource.TYPE,
        attributes = toJsonApiAccountAttributes(),
        relationships = LoanRelationshipTransformer().invoke(loans.filter { it.accountId == id }),
    )

internal fun Account.toJsonApiAccountAttributes() =
    AccountResource.Attributes(
        identityNumber = identityNumber.value,
        name = name.value,
        surname = surname.value,
        birthdate = birthdate.value,
        email = email.value,
        phonePrefix = phonePrefix.value,
        phoneNumber = phoneNumber.value,
        registerDate = registerDate.value,
        currentLoans = currentLoans.value,
        totalLoans = totalLoans.value,
    )

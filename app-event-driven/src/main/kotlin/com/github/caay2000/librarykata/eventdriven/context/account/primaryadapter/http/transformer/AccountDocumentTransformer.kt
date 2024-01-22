package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.cqrs.QueryHandler
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.http.shouldProcess
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQueryResponse
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.IncludeTransformer
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class AccountDocumentTransformer(loanRepository: LoanRepository) : Transformer<Account, JsonApiDocument<AccountResource>> {
    private val loanQueryHandler: QueryHandler<SearchLoanQuery, SearchLoanQueryResponse> = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Account,
        include: List<String>,
    ): JsonApiDocument<AccountResource> {
        // TODO When the Account is brand new, this query is not needed, as it won't have any relationship
        val loans = loanQueryHandler.invoke(SearchLoanQuery(value.id)).accounts
        return value.toJsonApiAccountDocument(loans, include)
    }
}

fun Account.toJsonApiAccountDocument(
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toJsonApiAccountResource(loans),
    included = if (include.shouldProcess(LoanResource.TYPE)) IncludeTransformer.invoke(emptyList()) else null,
)

internal fun Account.toJsonApiAccountResource(loans: Collection<Loan> = emptyList()) =
    AccountResource(
        id = id.value,
        type = AccountResource.TYPE,
        attributes = toJsonApiAccountAttributes(),
        relationships = RelationshipTransformer.invoke(loans.map { RelationshipIdentifier(it.id.value, LoanResource.TYPE) }),
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

package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import arrow.core.Either
import arrow.core.getOrElse
import com.github.caay2000.common.http.Transformer
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

internal class AccountResourceTransformer(loanRepository: LoanRepository) : Transformer<Account, AccountResource> {
    private val searchLoanQueryHandler: SearchLoanQueryHandler = SearchLoanQueryHandler(loanRepository)

    override fun invoke(
        value: Account,
        include: List<String>,
    ): AccountResource =
        AccountResource(
            id = value.id.value,
            type = AccountResource.TYPE,
            attributes = value.toJsonApiAccountAttributes(),
            relationships = RelationshipTransformer.invoke(retrieveAccountLoans(value.id).map { RelationshipIdentifier(it.id.value, LoanResource.TYPE) }),
        )

    private fun retrieveAccountLoans(id: AccountId): List<Loan> = Either.catch { searchLoanQueryHandler.invoke(SearchLoanQuery(id)).loans }.getOrElse { emptyList() }

    private fun Account.toJsonApiAccountAttributes() =
        AccountResource.Attributes(
            identityNumber = identityNumber.value,
            name = name.value,
            surname = surname.value,
            birthdate = birthdate.value,
            email = email.value,
            phone = phone.toString(),
            registerDate = registerDate.value,
            currentLoans = currentLoans.value,
            totalLoans = totalLoans.value,
        )
}

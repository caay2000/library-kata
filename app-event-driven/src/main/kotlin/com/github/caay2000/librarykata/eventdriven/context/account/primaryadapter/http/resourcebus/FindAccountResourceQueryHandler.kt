package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.resourcebus

import com.github.caay2000.common.resourcebus.ResourceBusQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.application.find.FindAccountQuery
import com.github.caay2000.librarykata.eventdriven.context.account.application.find.FindAccountQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQuery
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search.SearchLoanQueryHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class FindAccountResourceQueryHandler(
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : ResourceBusQueryHandler<AccountResource> {
    private val accountQueryHandler = FindAccountQueryHandler(accountRepository)
    private val loanQueryHandler = SearchLoanQueryHandler(loanRepository)

    override fun retrieve(
        identifier: String,
        includeRelationships: Boolean,
    ): AccountResource {
        val account = accountQueryHandler.invoke(FindAccountQuery(AccountId(identifier))).account

        val relationships = if (includeRelationships) loanQueryHandler.invoke(SearchLoanQuery(account.id)).accounts else emptyList()

        return account.toJsonApiAccountResource(relationships)
    }

    private fun Account.toJsonApiAccountResource(loans: Collection<Loan> = emptyList()) =
        AccountResource(
            id = id.value,
            type = AccountResource.TYPE,
            attributes = toJsonApiAccountAttributes(),
            relationships = RelationshipTransformer.invoke(loans.map { RelationshipIdentifier(it.id.value, LoanResource.TYPE) }),
        )

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

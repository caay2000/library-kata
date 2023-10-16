package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toLoanDocumentAttributes
import com.github.caay2000.librarykata.hexagonal.jsonMapper
import kotlinx.serialization.encodeToString

object AccountDocumentMother {

    fun random(account: Account = AccountMother.random()) = account.toAccountDocument()

    fun random(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
    ) = account.toAccountDocument(loans)

    fun json(account: Account = AccountMother.random()) = jsonMapper.encodeToString(account.toAccountDocument())

    fun json(
        account: Account = AccountMother.random(),
        loan: Loan,
    ) = jsonMapper.encodeToString(account.toAccountDocument(listOf(loan)))

    fun json(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
        included: List<String> = emptyList(),
    ) = jsonMapper.encodeToString(account.toAccountDocument(loans, included.map { it.uppercase() }))

    private fun Account.toAccountDocument(
        loans: List<Loan> = emptyList(),
        included: List<String> = emptyList(),
    ) = AccountDocument(
        data = AccountDocument.Resource(
            id = id.value,
            type = "account",
            attributes = AccountDocument.Resource.Attributes(
                identityNumber = identityNumber.value,
                name = name.value,
                surname = surname.value,
                birthdate = birthdate.value,
                email = email.value,
                phonePrefix = phonePrefix.value,
                phoneNumber = phoneNumber.value,
                registerDate = registerDate.value,
            ),
            relationships = loans.map {
                JsonApiRelationshipIdentifier(id = it.id.value, type = "loan")
            },
        ),
        included = if (included.contains("LOANS")) {
            loans.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = "loan",
                    attributes = it.toLoanDocumentAttributes(),
                )
            }
        } else {
            emptyList()
        },
    )
}

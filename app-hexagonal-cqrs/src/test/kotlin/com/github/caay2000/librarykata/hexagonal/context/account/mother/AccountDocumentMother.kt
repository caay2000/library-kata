package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentAttributes
import kotlinx.serialization.encodeToString

object AccountDocumentMother {

    fun random(account: Account = AccountMother.random()) = account.toJsonApiDocument()

    fun random(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
    ) = account.toJsonApiDocument(loans)

    fun json(account: Account = AccountMother.random()) = jsonMapper.encodeToString(account.toJsonApiDocument())

    fun json(
        account: Account = AccountMother.random(),
        loan: Loan,
    ) = jsonMapper.encodeToString(account.toJsonApiDocument(listOf(loan)))

    fun json(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
        included: List<String> = emptyList(),
    ) = jsonMapper.encodeToString(account.toJsonApiDocument(loans, included.map { it.uppercase() }))

    private fun Account.toJsonApiDocument(
        loans: List<Loan> = emptyList(),
        included: List<String> = emptyList(),
    ) = JsonApiDocument(
        data = AccountResource(
            id = id.value,
            type = "account",
            attributes = AccountResource.Attributes(
                identityNumber = identityNumber.value,
                name = name.value,
                surname = surname.value,
                birthdate = birthdate.value,
                email = email.value,
                phonePrefix = phonePrefix.value,
                phoneNumber = phoneNumber.value,
                registerDate = registerDate.value,
            ),
            relationships = mapRelationships(loans),
        ),
        included = mapIncluded(included, loans),
    )

    private fun mapIncluded(
        included: List<String>,
        loans: List<Loan>,
    ): List<JsonApiIncludedResource>? = if (included.contains("LOANS")) {
        if (loans.isEmpty()) {
            null
        } else {
            loans.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = "loan",
                    attributes = it.toJsonApiDocumentAttributes(),
                    relationships = null,
                )
            }
        }
    } else {
        null
    }

    private fun mapRelationships(loans: List<Loan>): Map<String, JsonApiRelationshipData>? =
        if (loans.isEmpty()) {
            null
        } else {
            mapOf(
                "loan" to JsonApiRelationshipData(
                    loans.map { JsonApiRelationshipIdentifier(id = it.id.value, type = "loan") },
                ),
            )
        }
}

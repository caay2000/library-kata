package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentAttributes

internal fun Account.toJsonApiDocument(
    loans: List<Loan> = emptyList(),
    included: List<String> = emptyList(),
) = JsonApiDocument(
    data = toAccountResource(loans),
    included = mapIncluded(included, loans),
)

internal fun Account.toAccountResource(loans: List<Loan> = emptyList()) =
    AccountResource(
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

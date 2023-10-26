package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan

internal fun Account.toJsonApiDocumentResource(loans: List<Loan> = emptyList()) =
    AccountResource(
        id = id.value,
        type = "account",
        attributes = toJsonApiDocumentAttributes(),
        relationships = mapRelationships(loans),
    )

private fun Account.toJsonApiDocumentAttributes() =
    AccountResource.Attributes(
        identityNumber = identityNumber.value,
        name = name.value,
        surname = surname.value,
        birthdate = birthdate.value,
        email = email.value,
        phonePrefix = phonePrefix.value,
        phoneNumber = phoneNumber.value,
        registerDate = registerDate.value,
    )

private fun List<Account>.toJsonApiDocumentIncludedResource() =
    map {
        AccountResource(
            id = it.id.value,
            type = "account",
            attributes = it.toJsonApiDocumentAttributes(),
        )
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

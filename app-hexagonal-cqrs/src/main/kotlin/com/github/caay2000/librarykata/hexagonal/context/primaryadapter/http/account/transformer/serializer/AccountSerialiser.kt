package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.serializer

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

internal fun Account.toJsonApiDocumentAccountResource(loans: List<Loan> = emptyList()) =
    AccountResource(
        id = id.value,
        type = "account",
        attributes = toJsonApiDocumentAccountAttributes(),
        relationships = mapRelationships(loans),
    )

private fun Account.toJsonApiDocumentAccountAttributes() =
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

// private fun List<Account>.toJsonApiDocumentIncludedResource() =
//    map {
//        AccountResource(
//            id = it.id.value,
//            type = "account",
//            attributes = it.toJsonApiDocumentAccountAttributes(),
//        )
//    }

private fun mapRelationships(loans: List<Loan>): Map<String, JsonApiRelationshipData>? =
    if (loans.isEmpty()) {
        null
    } else {
        mapOf(
            "loan" to
                JsonApiRelationshipData(
                    loans.map { JsonApiRelationshipIdentifier(id = it.id.value, type = "loan") },
                ),
        )
    }

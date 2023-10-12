package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import java.time.LocalDate
import java.time.LocalDateTime

data class AccountDocument(
    override val data: Resource,
    override val included: List<JsonApiRelationshipResource> = emptyList(),
) : JsonApiDocument {

    data class Resource(
        override val id: String,
        override val type: String = "account",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipIdentifier> = emptyList(),
    ) : JsonApiResource {

        data class Attributes(
            val identityNumber: String,
            val name: String,
            val surname: String,
            val birthdate: LocalDate,
            val email: String,
            val phonePrefix: String,
            val phoneNumber: String,
            val registerDate: LocalDateTime,
        ) : JsonApiResourceAttributes
    }
}

fun Account.toAccountDocument(loans: List<Loan> = emptyList()) =
    AccountDocument(
        data = AccountDocument.Resource(
            id = id.value,
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
        included = loans.map {
            JsonApiRelationshipResource(
                id = it.id.value,
                type = "loan",
                attributes = it.toLoanDocumentAttributes() as JsonApiResourceAttributes,
            )
        },
    )

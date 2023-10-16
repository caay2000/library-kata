package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountDocument(
    override val data: Resource,
    override val included: List<JsonApiIncludedResource> = emptyList(),
) : JsonApiDocument {

    @Serializable
    data class Resource(
        override val id: String,
        override val type: String = "account",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipIdentifier> = emptyList(),
    ) : JsonApiResource {

        @Serializable
        @SerialName("account")
        data class Attributes(
            val identityNumber: String,
            val name: String,
            val surname: String,
            @Serializable(with = LocalDateSerializer::class)
            val birthdate: LocalDate,
            val email: String,
            val phonePrefix: String,
            val phoneNumber: String,
            @Serializable(with = LocalDateTimeSerializer::class)
            val registerDate: LocalDateTime,
        ) : JsonApiResourceAttributes
    }
}

fun Account.toAccountDocument(loans: List<Loan> = emptyList()) =
    AccountDocument(
        data = toAccountDocumentResource(loans),
        included = loans.toLoanDocumentIncludedResource(),
    )

fun Account.toAccountDocumentResource(loans: List<Loan> = emptyList()) =
    AccountDocument.Resource(
        id = id.value,
        attributes = toAccountDocumentAttributes(),
        relationships = loans.map {
            JsonApiRelationshipIdentifier(id = it.id.value, type = "loan")
        },
    )

fun Account.toAccountDocumentAttributes() =
    AccountDocument.Resource.Attributes(
        identityNumber = identityNumber.value,
        name = name.value,
        surname = surname.value,
        birthdate = birthdate.value,
        email = email.value,
        phonePrefix = phonePrefix.value,
        phoneNumber = phoneNumber.value,
        registerDate = registerDate.value,
    )

fun List<Account>.toAccountDocumentIncludedResource() =
    map {
        JsonApiIncludedResource(
            id = it.id.value,
            type = "account",
            attributes = it.toAccountDocumentAttributes(),
        )
    }

package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountDocument(
    override val data: Resource,
    override val included: List<RelationshipResource> = emptyList(),
) : JsonApiDocument {

    @Serializable
    data class Resource(
        override val id: String,
        override val type: String = "account",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipResource> = emptyList(),
    ) : JsonApiResource {

        @Serializable
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

    @Serializable
    data class RelationshipResource(
        override val id: String,
        override val type: String,
        override val attributes: List<JsonApiResourceAttributes>,
    ) : JsonApiRelationshipResource
}

fun Account.toAccountDocument() = AccountDocument(
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
    ),
)

package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiAttributes
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountDocument(
    override val data: AccountResource,
    override val included: List<AccountRelationshipResource> = emptyList(),
) : JsonApiDocument

@Serializable
data class AccountResource(
    override val id: String,
    override val type: String = "account",
    override val attributes: AccountAttributes,
    override val relationships: List<JsonApiRelationshipResource> = emptyList(),
) : JsonApiResource

@Serializable
data class AccountAttributes(
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
) : JsonApiAttributes

@Serializable
data class AccountRelationshipResource(
    override val id: String,
    override val type: String,
    override val attributes: List<JsonApiAttributes>,
) : JsonApiRelationshipResource

fun Account.toAccountDocument() = AccountDocument(
    data = AccountResource(
        id = id.value,
        attributes = AccountAttributes(
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

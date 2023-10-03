package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import com.github.caay2000.common.serialization.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class AccountRequestDocument(
    override val data: AccountRequestResource,
) : JsonApiRequestDocument

@Serializable
data class AccountRequestResource(
    override val type: String = "account",
    override val attributes: AccountRequestAttributes,
) : JsonApiRequestResource

@Serializable
data class AccountRequestAttributes(
    val identityNumber: String,
    val name: String,
    val surname: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthdate: LocalDate,
    val email: String,
    val phonePrefix: String,
    val phoneNumber: String,
) : JsonApiRequestAttributes

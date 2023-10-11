package com.github.caay2000.librarykata.hexagonalarrow.context.primaryadapter.http.serialization

import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Account
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountDetailsDocument(
    val id: String,
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
)

fun Account.toAccountDetailsDocument() = AccountDetailsDocument(
    id = id.value,
    identityNumber = identityNumber.value,
    name = name.value,
    surname = surname.value,
    birthdate = birthdate.value,
    email = email.value,
    phonePrefix = phonePrefix.value,
    phoneNumber = phoneNumber.value,
    registerDate = registerDate.value,
)

@Serializable
data class CreateAccountRequestDocument(
    val identityNumber: String,
    val name: String,
    val surname: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthdate: LocalDate,
    val email: String,
    val phonePrefix: String,
    val phoneNumber: String,
)

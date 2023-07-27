package com.github.caay2000.projectskeleton.context.account.primaryadapter.http

import com.github.caay2000.common.serialization.LocalDateSerializer
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.projectskeleton.context.account.domain.Account
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class AccountDetailsDocument(
    val accountNumber: String,
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
    val name: String,
    val surname: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    @Serializable(with = LocalDateTimeSerializer::class)
    val registerDate: LocalDateTime,
)

fun Account.toAccountDetailsDocument() = AccountDetailsDocument(
    accountNumber = accountNumber.value,
    email = email.value,
    phoneNumber = phoneNumber.value,
    phonePrefix = phonePrefix.value,
    name = name.value,
    surname = surname.value,
    birthDate = birthDate.value,
    registerDate = registerDate.value,
)

@Serializable
data class CreateAccountRequestDocument(
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
    val name: String,
    val surname: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
)

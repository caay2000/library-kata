package com.github.caay2000.librarykata.hexagonal.context.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class Account(
    val id: AccountId,
    val identityNumber: IdentityNumber,
    val name: Name,
    val surname: Surname,
    val birthdate: Birthdate,
    val email: Email,
    val phonePrefix: PhonePrefix,
    val phoneNumber: PhoneNumber,
    val registerDate: RegisterDate,
) {

    companion object {
        fun create(request: CreateAccountRequest) = Account(
            id = request.accountId,
            identityNumber = request.identityNumber,
            name = request.name,
            surname = request.surname,
            birthdate = request.birthdate,
            email = request.email,
            phonePrefix = request.phonePrefix,
            phoneNumber = request.phoneNumber,
            registerDate = request.registerDate,
        )
    }
}

@JvmInline
value class AccountId(val value: String)

@JvmInline
value class IdentityNumber(val value: String)

@JvmInline
value class Email(val value: String)

@JvmInline
value class PhoneNumber(val value: String)

@JvmInline
value class PhonePrefix(val value: String)

@JvmInline
value class Name(val value: String)

@JvmInline
value class Surname(val value: String)

@JvmInline
value class Birthdate(val value: LocalDate)

@JvmInline
value class RegisterDate(val value: LocalDateTime)

data class CreateAccountRequest(
    val accountId: AccountId,
    val identityNumber: IdentityNumber,
    val name: Name,
    val surname: Surname,
    val birthdate: Birthdate,
    val email: Email,
    val phonePrefix: PhonePrefix,
    val phoneNumber: PhoneNumber,
    val registerDate: RegisterDate,
)

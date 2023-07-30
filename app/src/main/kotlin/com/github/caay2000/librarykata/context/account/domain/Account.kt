package com.github.caay2000.librarykata.context.account.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.librarykata.events.account.AccountCreatedEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class Account(
    override val id: AccountId,
    val identityNumber: IdentityNumber,
    val name: Name,
    val surname: Surname,
    val birthdate: Birthdate,
    val email: Email,
    val phonePrefix: PhonePrefix,
    val phoneNumber: PhoneNumber,
    val registerDate: RegisterDate,
) : Aggregate() {

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
        ).also { account -> account.pushEvent(account.toAccountCreatedEvent()) }
    }

    private fun toAccountCreatedEvent() = AccountCreatedEvent(
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
}

@JvmInline
value class AccountId(val value: UUID) : DomainId {
    override fun toString(): String = value.toString()
}

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

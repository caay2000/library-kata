package com.github.caay2000.projectskeleton.context.account.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.common.event.events.account.AccountCreatedEvent
import java.time.LocalDate
import java.time.LocalDateTime

data class Account(
    val accountNumber: AccountNumber,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val phonePrefix: PhonePrefix,
    val name: Name,
    val surname: Surname,
    val birthDate: BirthDate,
    val registerDate: RegisterDate,
) : Aggregate() {

    override val id: DomainId
        get() = accountNumber

    companion object {
        fun create(request: CreateAccountRequest) = Account(
            accountNumber = request.accountNumber,
            email = request.email,
            phoneNumber = request.phoneNumber,
            phonePrefix = request.phonePrefix,
            name = request.name,
            surname = request.surname,
            birthDate = request.birthDate,
            registerDate = request.registerDate,
        ).also { account -> account.pushEvent(account.toAccountCreatedEvent()) }
    }

    private fun toAccountCreatedEvent() = AccountCreatedEvent(
        accountNumber = accountNumber.value,
        email = email.value,
        phoneNumber = phoneNumber.value,
        phonePrefix = phonePrefix.value,
        name = name.value,
        surname = surname.value,
        birthDate = birthDate.value,
        registerDate = registerDate.value,
    )
}

@JvmInline
value class AccountNumber(val value: String) : DomainId

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
value class BirthDate(val value: LocalDate)

@JvmInline
value class RegisterDate(val value: LocalDateTime)

data class CreateAccountRequest(
    val accountNumber: AccountNumber,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val phonePrefix: PhonePrefix,
    val name: Name,
    val surname: Surname,
    val birthDate: BirthDate,
    val registerDate: RegisterDate,
)

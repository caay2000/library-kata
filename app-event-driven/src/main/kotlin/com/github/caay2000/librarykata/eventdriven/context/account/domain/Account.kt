package com.github.caay2000.librarykata.eventdriven.context.account.domain

import com.github.caay2000.common.date.Date
import com.github.caay2000.common.date.DateTime
import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.AggregateId
import com.github.caay2000.librarykata.eventdriven.events.account.AccountCreatedEvent
import kotlin.math.max

data class Account(
    override val id: AccountId,
    val identityNumber: IdentityNumber,
    val name: Name,
    val surname: Surname,
    val birthdate: Birthdate,
    val email: Email,
    val phone: Phone,
    val registerDate: RegisterDate,
    val currentLoans: CurrentLoans,
    val totalLoans: TotalLoans,
) : Aggregate() {
    companion object {
        fun create(request: CreateAccountRequest) =
            Account(
                id = request.accountId,
                identityNumber = request.identityNumber,
                name = request.name,
                surname = request.surname,
                birthdate = request.birthdate,
                email = request.email,
                phone = Phone.create(request.phonePrefix.value, request.phoneNumber.value),
                registerDate = request.registerDate,
                currentLoans = CurrentLoans(0),
                totalLoans = TotalLoans(0),
            ).also { account -> account.pushEvent(account.toAccountCreatedEvent()) }
    }

    private fun toAccountCreatedEvent() =
        AccountCreatedEvent(
            id = id.value,
            identityNumber = identityNumber.value,
            name = name.value,
            surname = surname.value,
            birthdate = birthdate.value,
            email = email.value,
            phone = phone.toString(),
            registerDate = registerDate.value,
        )

    fun increaseLoans(): Account = copy(currentLoans = currentLoans.increase(), totalLoans = totalLoans.increase())

    fun decreaseLoans(): Account = copy(currentLoans = currentLoans.decrease())
}

@JvmInline
value class AccountId(val value: String) : AggregateId

@JvmInline
value class IdentityNumber(val value: String)

@JvmInline
value class Email(val value: String)

data class Phone(val prefix: PhonePrefix, val number: PhoneNumber) {
    companion object {
        fun create(
            prefix: String,
            number: String,
        ): Phone = Phone(PhonePrefix(prefix), PhoneNumber(number))
    }

    override fun toString(): String {
        return "$prefix $number"
    }
}

@JvmInline
value class PhoneNumber(val value: String)

@JvmInline
value class PhonePrefix(val value: String)

@JvmInline
value class Name(val value: String)

@JvmInline
value class Surname(val value: String)

@JvmInline
value class CurrentLoans(val value: Int) {
    internal fun increase(value: Int = 1): CurrentLoans = CurrentLoans(this.value + value)

    internal fun decrease(value: Int = 1): CurrentLoans = CurrentLoans(max(this.value - value, 0))
}

@JvmInline
value class TotalLoans(val value: Int) {
    internal fun increase(value: Int = 1): TotalLoans = TotalLoans(this.value + value)
}

typealias Birthdate = Date
typealias RegisterDate = DateTime

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

package com.github.caay2000.librarykata.hexagonal.context.account.domain

import com.github.caay2000.common.date.Date
import com.github.caay2000.common.date.DateTime
import kotlin.math.max

data class Account(
    val id: AccountId,
    val identityNumber: IdentityNumber,
    val name: Name,
    val surname: Surname,
    val birthdate: Birthdate,
    val email: Email,
    val phone: Phone,
    val registerDate: RegisterDate,
    val currentLoans: CurrentLoans,
    val totalLoans: TotalLoans,
) {
    companion object {
        fun create(request: CreateAccountRequest) =
            Account(
                id = request.accountId,
                identityNumber = request.identityNumber,
                name = request.name,
                surname = request.surname,
                birthdate = request.birthdate,
                email = request.email,
                phone = request.phone,
                registerDate = request.registerDate,
                currentLoans = CurrentLoans(0),
                totalLoans = TotalLoans(0),
            )
    }

    // TODO we should validate inputs in their own VO, for example email should have an email format and birthdate cannot be in the future

    fun increaseLoans(): Account = copy(currentLoans = currentLoans.increase(), totalLoans = totalLoans.increase())

    fun decreaseLoans(): Account = copy(currentLoans = currentLoans.decrease())

    fun hasReachedLoanLimit(): Boolean = currentLoans.value >= 5
}

@JvmInline
value class AccountId(val value: String)

@JvmInline
value class IdentityNumber(val value: String)

@JvmInline
value class Email(val value: String)
// TODO validate email format

data class Phone(val prefix: PhonePrefix, val number: PhoneNumber) {
    // TODO validate phone format
    companion object {
        fun create(
            prefix: String,
            number: String,
        ): Phone = Phone(PhonePrefix(prefix), PhoneNumber(number))
    }

    override fun toString(): String {
        return "${prefix.value} ${number.value}"
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

typealias Birthdate = Date
// TODO validate birthdate in the future or minimum age

typealias RegisterDate = DateTime
// TODO validate registerDate in the future

@JvmInline
value class CurrentLoans(val value: Int) {
    internal fun increase(value: Int = 1): CurrentLoans = CurrentLoans(this.value + value)

    internal fun decrease(value: Int = 1): CurrentLoans = CurrentLoans(max(this.value - value, 0))
}

@JvmInline
value class TotalLoans(val value: Int) {
    internal fun increase(value: Int = 1): TotalLoans = TotalLoans(this.value + value)
}

data class CreateAccountRequest(
    val accountId: AccountId,
    val identityNumber: IdentityNumber,
    val name: Name,
    val surname: Surname,
    val birthdate: Birthdate,
    val email: Email,
    val phone: Phone,
    val registerDate: RegisterDate,
)

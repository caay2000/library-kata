package com.github.caay2000.librarykata.eventdriven.context.loan.domain

import kotlin.math.max

data class Account(
    val id: AccountId,
    val currentLoans: CurrentLoans,
) {
    companion object {
        fun create(accountId: AccountId) = Account(accountId, CurrentLoans(0))
    }

    fun increaseLoans(): Account = copy(currentLoans = currentLoans.increase())

    fun decreaseLoans(): Account = copy(currentLoans = currentLoans.decrease())

    fun hasReachedLoanLimit(): Boolean = currentLoans.value >= 5
}

@JvmInline
value class AccountId(val value: String)

@JvmInline
value class CurrentLoans(val value: Int) {
    internal fun increase(value: Int = 1): CurrentLoans = CurrentLoans(this.value + value)

    internal fun decrease(value: Int = 1): CurrentLoans = CurrentLoans(max(this.value - value, 0))
}

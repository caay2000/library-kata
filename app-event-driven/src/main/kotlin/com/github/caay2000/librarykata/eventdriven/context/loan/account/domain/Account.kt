package com.github.caay2000.librarykata.eventdriven.context.loan.account.domain

import com.github.caay2000.common.ddd.AggregateId
import kotlin.math.max

data class Account(
    val id: AccountId,
    val currentLoans: CurrentLoans,
) {
    companion object {
        fun create(accountId: AccountId) = Account(accountId, CurrentLoans(0))
    }
}

@JvmInline
value class AccountId(val value: String) : AggregateId

@JvmInline
value class CurrentLoans(val value: Int) {
    internal fun increase(value: Int = 1): CurrentLoans = CurrentLoans(this.value + value)

    internal fun decrease(value: Int = 1): CurrentLoans = CurrentLoans(max(this.value - value, 0))
}

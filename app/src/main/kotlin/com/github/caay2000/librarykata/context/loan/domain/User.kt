package com.github.caay2000.librarykata.context.loan.domain

import java.util.UUID
import kotlin.math.abs

data class User(
    val id: UserId,
    val currentLoans: CurrentLoans,
) {

    fun updateCurrentLoans(value: Int): User =
        if (value > 0) {
            copy(currentLoans = currentLoans.increase(value))
        } else {
            copy(currentLoans = currentLoans.decrease(abs(value)))
        }

    fun hasReachedLoanLimit(): Boolean = currentLoans.value >= 5

    companion object {
        fun create(id: UserId) = User(id = id, currentLoans = CurrentLoans(0))
    }
}

@JvmInline
value class UserId(val value: UUID) {
    override fun toString(): String = value.toString()
}

@JvmInline
value class CurrentLoans(val value: Int) {
    fun increase(value: Int): CurrentLoans = CurrentLoans(this.value + value)
    fun decrease(value: Int): CurrentLoans = CurrentLoans(Math.max(this.value - value, 0))
}

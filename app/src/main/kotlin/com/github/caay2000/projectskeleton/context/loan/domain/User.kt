package com.github.caay2000.projectskeleton.context.loan.domain

import java.util.UUID

data class User(
    val id: UserId,
    val currentLoans: Int,
) {

    companion object {
        fun create(id: UserId) = User(id = id, currentLoans = 0)
    }
}

@JvmInline
value class UserId(val value: UUID) {
    override fun toString(): String = value.toString()
}

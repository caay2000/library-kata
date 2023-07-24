package com.github.caay2000.projectskeleton.context.account.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.common.event.events.account.AccountCreatedEvent
import java.util.UUID

data class Account(
    override val id: AccountId,
    val email: Email,
    val name: Name,
) : Aggregate() {

    companion object {
        fun create(request: CreateAccountRequest) = Account(
            id = request.id,
            email = request.email,
            name = request.name,
        ).also { account -> account.pushEvent(account.toAccountCreatedEvent()) }
    }

    private fun toAccountCreatedEvent() = AccountCreatedEvent(
        accountId = id.value,
        email = email.toString(),
        name = name.toString(),
    )
}

@JvmInline
value class AccountId(val value: UUID) : DomainId {
    override fun toString(): String = value.toString()
}

@JvmInline
value class Email(val value: String) {
    override fun toString(): String = value
}

@JvmInline
value class Name(val value: String) {
    override fun toString(): String = value
}

data class CreateAccountRequest(
    val id: AccountId,
    val email: Email,
    val name: Name,
)

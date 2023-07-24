package com.github.caay2000.common.event.events.account

import com.github.caay2000.common.event.api.DomainEvent
import java.util.UUID

data class AccountCreatedEvent(
    val accountId: UUID,
    val email: String,
    val name: String,
) : DomainEvent(accountId)

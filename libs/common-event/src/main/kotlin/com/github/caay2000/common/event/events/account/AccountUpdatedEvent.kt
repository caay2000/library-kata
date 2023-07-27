package com.github.caay2000.common.event.events.account

import com.github.caay2000.common.event.api.DomainEvent
import java.time.LocalDateTime

data class AccountUpdatedEvent(
    val accountNumber: String,
    val updatedFields: Map<String, String>,
    val updatedDate: LocalDateTime,
) : DomainEvent(accountNumber)

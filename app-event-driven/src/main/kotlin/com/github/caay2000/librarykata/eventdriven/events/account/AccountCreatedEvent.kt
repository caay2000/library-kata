package com.github.caay2000.librarykata.eventdriven.events.account

import com.github.caay2000.common.event.DomainEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class AccountCreatedEvent(
    val id: UUID,
    val identityNumber: String,
    val name: String,
    val surname: String,
    val birthdate: LocalDate,
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
    val registerDate: LocalDateTime,
) : DomainEvent(id)

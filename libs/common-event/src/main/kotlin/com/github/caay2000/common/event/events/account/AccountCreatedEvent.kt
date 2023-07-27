package com.github.caay2000.common.event.events.account

import com.github.caay2000.common.event.api.DomainEvent
import java.time.LocalDate
import java.time.LocalDateTime

data class AccountCreatedEvent(
    val accountNumber: String,
    val email: String,
    val phoneNumber: String,
    val phonePrefix: String,
    val name: String,
    val surname: String,
    val birthDate: LocalDate,
    val registerDate: LocalDateTime,
) : DomainEvent(accountNumber)

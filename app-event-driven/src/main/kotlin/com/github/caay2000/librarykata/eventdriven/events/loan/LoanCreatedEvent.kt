package com.github.caay2000.librarykata.eventdriven.events.loan

import com.github.caay2000.common.event.DomainEvent
import java.time.LocalDateTime

data class LoanCreatedEvent(
    val loanId: String,
    val bookId: String,
    val accountId: String,
    val createdAt: LocalDateTime,
) : DomainEvent(loanId)

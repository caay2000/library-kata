package com.github.caay2000.librarykata.eventdriven.events.loan

import com.github.caay2000.common.event.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

data class LoanFinishedEvent(
    val loanId: UUID,
    val bookId: UUID,
    val userId: UUID,
    val finishedAt: LocalDateTime,
) : DomainEvent(loanId.toString())

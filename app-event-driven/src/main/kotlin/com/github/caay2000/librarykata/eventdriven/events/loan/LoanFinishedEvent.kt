package com.github.caay2000.librarykata.eventdriven.events.loan

import com.github.caay2000.common.event.DomainEvent
import java.time.LocalDateTime

data class LoanFinishedEvent(
    val loanId: String,
    val bookId: String,
    val accountId: String,
    val finishedAt: LocalDateTime,
) : DomainEvent(loanId)

package com.github.caay2000.librarykata.events.loan

import com.github.caay2000.common.event.DomainEvent
import java.util.UUID

data class LoanCreatedEvent(
    val loanId: UUID,
    val bookId: UUID,
    val userId: UUID,
) : DomainEvent(loanId)

package com.github.caay2000.librarykata.context.account.domain

import java.time.LocalDateTime
import java.util.UUID

data class Loan(
    val id: LoanId,
    val accountId: AccountId,
    val bookId: BookId,
    val startedAt: StartLoanDateTime,
    val finishedAt: FinishLoanDateTime? = null,
) {

    fun finish(returnedAt: FinishLoanDateTime): Loan =
        copy(finishedAt = returnedAt)
}

@JvmInline
value class LoanId(val value: UUID)

@JvmInline
value class StartLoanDateTime(val value: LocalDateTime)

@JvmInline
value class FinishLoanDateTime(val value: LocalDateTime)

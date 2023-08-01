package com.github.caay2000.librarykata.context.book.domain

import java.time.LocalDateTime
import java.util.UUID

data class Loan(
    val id: LoanId,
    val bookId: BookId,
    val userId: UserId,
    val startedAt: StartLoanDateTime,
    val finishedAt: FinishLoanDateTime? = null,
) {

    companion object {
        fun create(
            id: LoanId,
            bookId: BookId,
            userId: UserId,
            startedAt: StartLoanDateTime,
        ) = Loan(
            id = id,
            bookId = bookId,
            userId = userId,
            startedAt = startedAt,
        )
    }

    fun finish(returnedAt: FinishLoanDateTime): Loan =
        copy(finishedAt = returnedAt)
}

@JvmInline
value class LoanId(val value: UUID)

@JvmInline
value class UserId(val value: UUID)

@JvmInline
value class StartLoanDateTime(val value: LocalDateTime)

@JvmInline
value class FinishLoanDateTime(val value: LocalDateTime)

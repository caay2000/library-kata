package com.github.caay2000.librarykata.hexagonal.context.domain.loan

import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import java.time.LocalDateTime

data class Loan(
    val id: LoanId,
    val bookId: BookId,
    val accountId: AccountId,
    val createdAt: CreatedAt,
    val finishedAt: FinishedAt?,
) {
    companion object {
        fun create(
            id: LoanId,
            bookId: BookId,
            accountId: AccountId,
            createdAt: CreatedAt,
        ) = Loan(
            id = id,
            bookId = bookId,
            accountId = accountId,
            createdAt = createdAt,
            finishedAt = null,
        )
    }

    val isFinished: Boolean = finishedAt != null
    val isNotFinished: Boolean = isFinished.not()

    fun finishLoan(finishedAt: FinishedAt) = copy(finishedAt = finishedAt)
}

@JvmInline
value class LoanId(val value: String)

@JvmInline
value class CreatedAt(val value: LocalDateTime)

@JvmInline
value class FinishedAt(val value: LocalDateTime)

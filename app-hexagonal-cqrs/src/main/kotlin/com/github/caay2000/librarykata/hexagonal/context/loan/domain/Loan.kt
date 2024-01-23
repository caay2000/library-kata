package com.github.caay2000.librarykata.hexagonal.context.loan.domain

import com.github.caay2000.common.date.DateTime
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId

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

typealias CreatedAt = DateTime
typealias FinishedAt = DateTime

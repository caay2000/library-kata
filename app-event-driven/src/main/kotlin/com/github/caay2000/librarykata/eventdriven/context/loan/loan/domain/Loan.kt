package com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.AggregateId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanCreatedEvent
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanFinishedEvent
import java.time.LocalDateTime

data class Loan(
    override val id: LoanId,
    val bookId: BookId,
    val accountId: AccountId,
    val createdAt: CreatedAt,
    val finishedAt: FinishedAt?,
) : Aggregate() {
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
        ).also { loan -> loan.pushEvent(loan.toLoanCreatedEvent()) }
    }

    val isFinished: Boolean = finishedAt != null
    val isNotFinished: Boolean = isFinished.not()

    fun finishLoan(finishedAt: FinishedAt) =
        copy(finishedAt = finishedAt)
            .also { loan -> loan.pushEvent(loan.toLoanFinishedEvent()) }

    private fun toLoanCreatedEvent() =
        LoanCreatedEvent(
            loanId = id.value,
            bookId = bookId.value,
            accountId = accountId.value,
            createdAt = createdAt.value,
        )

    private fun toLoanFinishedEvent() =
        LoanFinishedEvent(
            loanId = id.value,
            bookId = bookId.value,
            accountId = accountId.value,
            finishedAt = finishedAt!!.value,
        )
}

@JvmInline
value class LoanId(val value: String) : AggregateId

@JvmInline
value class CreatedAt(val value: LocalDateTime)

@JvmInline
value class FinishedAt(val value: LocalDateTime)

package com.github.caay2000.librarykata.event.context.loan.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.librarykata.event.events.loan.LoanCreatedEvent
import com.github.caay2000.librarykata.event.events.loan.LoanFinishedEvent
import java.time.LocalDateTime
import java.util.UUID

data class Loan(
    override val id: LoanId,
    val bookId: BookId,
    val userId: UserId,
    val createdAt: CreatedAt,
    val finishedAt: FinishedAt?,
) : Aggregate() {
    companion object {
        fun create(
            id: LoanId,
            bookId: BookId,
            userId: UserId,
            createdAt: CreatedAt,
        ) = Loan(
            id = id,
            bookId = bookId,
            userId = userId,
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
            userId = userId.value,
            createdAt = createdAt.value,
        )

    private fun toLoanFinishedEvent() =
        LoanFinishedEvent(
            loanId = id.value,
            bookId = bookId.value,
            userId = userId.value,
            finishedAt = finishedAt!!.value,
        )
}

@JvmInline
value class LoanId(val value: UUID) : DomainId {
    override fun toString(): String = value.toString()
}

@JvmInline
value class BookId(val value: UUID) {
    override fun toString(): String = value.toString()
}

@JvmInline
value class CreatedAt(val value: LocalDateTime)

@JvmInline
value class FinishedAt(val value: LocalDateTime)

package com.github.caay2000.librarykata.eventdriven.context.loan.application.finish

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.FindLoanCriteria
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository

class LoanFinisher(
    private val loanRepository: LoanRepository,
    private val eventPublisher: DomainEventPublisher,
) {
    fun invoke(
        bookId: BookId,
        finishedAt: FinishedAt,
    ): Either<LoanFinisherError, Unit> =
        findLoan(bookId)
            .map { loan -> loan.finishLoan(finishedAt) }
            .map { loan -> loanRepository.save(loan) }
            .map { loan -> eventPublisher.publish(loan.pullEvents()) }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, Loan> =
        loanRepository.find(FindLoanCriteria.ByBookIdAndNotFinished(bookId))
            ?.right()
            ?: LoanFinisherError.LoanNotFound(bookId).left()
}

sealed class LoanFinisherError(message: String) : RuntimeException(message) {
    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
}

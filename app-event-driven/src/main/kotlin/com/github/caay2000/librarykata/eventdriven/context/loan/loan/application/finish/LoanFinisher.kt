package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.finish

import arrow.core.Either
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.FindLoanCriteria
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.FinishedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.findOrElse

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
        loanRepository.findOrElse(
            criteria = FindLoanCriteria.ByBookIdAndNotFinished(bookId),
            onResourceDoesNotExist = { LoanFinisherError.LoanNotFound(bookId) },
        )
}

sealed class LoanFinisherError(message: String) : RuntimeException(message) {
    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
}

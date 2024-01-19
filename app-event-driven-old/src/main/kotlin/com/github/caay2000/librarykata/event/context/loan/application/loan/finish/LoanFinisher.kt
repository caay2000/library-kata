package com.github.caay2000.librarykata.event.context.loan.application.loan.finish

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.event.context.loan.application.FindLoanCriteria
import com.github.caay2000.librarykata.event.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.event.context.loan.domain.BookId
import com.github.caay2000.librarykata.event.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.event.context.loan.domain.Loan

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
            .map { loan -> loan.save() }
            .map { loan -> loan.publishEvents() }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, Loan> =
        loanRepository.findBy(FindLoanCriteria.ByBookIdAndNotFinished(bookId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanFinisherError.LoanNotFound(bookId)
                    else -> throw error
                }
            }

    private fun Loan.save(): Loan = loanRepository.save(this)

    private fun Loan.publishEvents() {
        eventPublisher.publish(pullEvents())
    }
}

sealed class LoanFinisherError(message: String) : RuntimeException(message) {
    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
}

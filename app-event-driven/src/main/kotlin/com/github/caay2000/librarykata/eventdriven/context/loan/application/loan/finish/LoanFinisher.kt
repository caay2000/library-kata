package com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.finish

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.loan.application.FindLoanCriteria
import com.github.caay2000.librarykata.eventdriven.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan

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
            .flatMap { loan -> loan.save() }
            .flatMap { loan -> loan.publishEvents() }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, Loan> =
        loanRepository.findBy(FindLoanCriteria.ByBookIdAndNotFinished(bookId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanFinisherError.LoanNotFound(bookId)
                    else -> LoanFinisherError.UnknownError(error)
                }
            }

    private fun Loan.save(): Either<LoanFinisherError, Loan> =
        loanRepository.save(this)
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.finish.LoanFinisherError.UnknownError(it) }
            .map { this }

    private fun Loan.publishEvents(): Either<LoanFinisherError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.finish.LoanFinisherError.UnknownError(it) }
}

sealed class LoanFinisherError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
    class UnknownError(error: Throwable) : LoanFinisherError(error)
}

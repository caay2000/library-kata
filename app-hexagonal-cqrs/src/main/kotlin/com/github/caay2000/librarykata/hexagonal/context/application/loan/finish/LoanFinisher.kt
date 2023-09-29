package com.github.caay2000.librarykata.hexagonal.context.application.loan.finish

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookAvailable
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan

class LoanFinisher(
    private val loanRepository: LoanRepository,
    private val bookRepository: BookRepository,
    private val accountRepository: AccountRepository,
) {

    fun invoke(
        bookId: BookId,
        finishedAt: FinishedAt,
    ): Either<LoanFinisherError, Unit> =
        findLoan(bookId)
            .map { loan -> loan.finishLoan(finishedAt) }
            .flatMap { loan -> loan.save() }
            .flatMap { loan -> findAccount(loan.accountId) }
            .map { account -> account.updateCurrentLoans() }
            .flatMap { account -> account.save() }
            .flatMap { findBook(bookId) }
            .map { book -> book.updateToAvailable() }
            .flatMap { book -> book.save() }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, Loan> =
        loanRepository.find(FindLoanCriteria.ByBookIdAndNotFinished(bookId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanFinisherError.LoanNotFound(bookId)
                    else -> LoanFinisherError.UnknownError(error)
                }
            }

    private fun findAccount(accountId: AccountId): Either<LoanFinisherError, Account> =
        accountRepository.find(FindAccountCriteria.ById(accountId))
            .mapLeft { LoanFinisherError.UnknownError(it) }

    private fun Account.updateCurrentLoans() = copy(currentLoans = currentLoans.decrease())

    private fun findBook(bookId: BookId): Either<LoanFinisherError, Book> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            .mapLeft { LoanFinisherError.UnknownError(it) }

    private fun Loan.save(): Either<LoanFinisherError, Loan> =
        loanRepository.save(this)
            .mapLeft { LoanFinisherError.UnknownError(it) }
            .map { this }

    private fun Account.save(): Either<LoanFinisherError, Unit> =
        accountRepository.save(this)
            .mapLeft { LoanFinisherError.UnknownError(it) }

    private fun Book.updateToAvailable(): Book = updateAvailability(BookAvailable.available())

    private fun Book.save(): Either<LoanFinisherError, Unit> =
        bookRepository.save(this)
            .mapLeft { LoanFinisherError.UnknownError(it) }
}

sealed class LoanFinisherError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
    class UnknownError(error: Throwable) : LoanFinisherError(error)
}

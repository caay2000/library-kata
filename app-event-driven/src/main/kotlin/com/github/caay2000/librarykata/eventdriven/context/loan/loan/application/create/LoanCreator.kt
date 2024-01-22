package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.findOrElse
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.CreatedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository

class LoanCreator(
    private val accountRepository: AccountRepository,
    private val bookRepository: BookRepository,
    private val loanRepository: LoanRepository,
    private val eventPublisher: DomainEventPublisher,
) {
    fun invoke(
        loanId: LoanId,
        accountId: AccountId,
        bookIsbn: BookIsbn,
        createdAt: CreatedAt,
    ): Either<LoanCreatorError, Unit> =
        guardAccountCurrentLoans(accountId)
            .map { searchBook(bookIsbn) }
            .flatMap { books -> guardBookAvailability(books, bookIsbn) }
            .map { book ->
                Loan.create(
                    id = loanId,
                    bookId = book.id,
                    accountId = accountId,
                    createdAt = createdAt,
                )
            }
            .map { loan -> loanRepository.save(loan) }
            .map { loan -> eventPublisher.publish(loan.pullEvents()) }

    private fun guardAccountCurrentLoans(accountId: AccountId): Either<LoanCreatorError, Unit> =
        accountRepository.findOrElse(
            id = accountId,
            onResourceDoesNotExist = { LoanCreatorError.AccountNotFound(accountId) },
        ).flatMap { account ->
            if (account.hasReachedLoanLimit()) LoanCreatorError.AccountHasTooManyLoans(account.id).left() else Unit.right()
        }

    private fun searchBook(bookIsbn: BookIsbn): List<Book> = bookRepository.search(bookIsbn)

    private fun guardBookAvailability(
        books: List<Book>,
        bookIsbn: BookIsbn,
    ): Either<LoanCreatorError, Book> =
        when {
            books.isEmpty() -> LoanCreatorError.BookNotFound(bookIsbn).left()
            books.none { it.isAvailable } -> LoanCreatorError.BookNotAvailable(bookIsbn).left()
            else -> books.first { it.isAvailable }.right()
        }
}

sealed class LoanCreatorError(message: String) : RuntimeException(message) {
    class BookNotAvailable(bookIsbn: BookIsbn) : LoanCreatorError("Book with isbn ${bookIsbn.value} is not available")

    class BookNotFound(bookIsbn: BookIsbn) : LoanCreatorError("Book with isbn ${bookIsbn.value} not found")

    class AccountNotFound(accountId: AccountId) : LoanCreatorError("Account ${accountId.value} not found")

    class AccountHasTooManyLoans(accountId: AccountId) : LoanCreatorError("Account ${accountId.value} has too many loans")
}

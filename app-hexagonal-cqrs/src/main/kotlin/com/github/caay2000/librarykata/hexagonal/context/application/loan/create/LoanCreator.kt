package com.github.caay2000.librarykata.hexagonal.context.application.loan.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.arrow.firstOrElse
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.account.findOrElse
import com.github.caay2000.librarykata.hexagonal.context.application.account.saveOrElse
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.SearchBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.book.saveOrElse
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.saveOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.LoanId

class LoanCreator(
    private val bookRepository: BookRepository,
    private val accountRepository: AccountRepository,
    private val loanRepository: LoanRepository,
) {

    fun invoke(
        loanId: LoanId,
        accountId: AccountId,
        bookIsbn: BookIsbn,
        createdAt: CreatedAt,
    ): Either<LoanCreatorError, Unit> =
        guardAccountCurrentLoans(accountId)
            .flatMap { ctx -> ctx.guardBookAvailability(bookIsbn) }
            .map { ctx -> ctx.createLoan(loanId = loanId, createdAt = createdAt) }
            .flatMap { ctx -> ctx.save() }

    private fun guardAccountCurrentLoans(accountId: AccountId): Either<LoanCreatorError, LoanCreatorContext> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ById(accountId),
            onResourceDoesNotExist = { LoanCreatorError.UserNotFound(accountId) },
            onUnexpectedError = { LoanCreatorError.UnknownError(it) },
        ).flatMap { account ->
            if (account.hasReachedLoanLimit()) {
                LoanCreatorError.UserHasTooManyLoans(account.id).left()
            } else {
                LoanCreatorContext().withAccount(account).right()
            }
        }

    private fun LoanCreatorContext.guardBookAvailability(bookIsbn: BookIsbn): Either<LoanCreatorError, LoanCreatorContext> =
        bookRepository.search(SearchBookCriteria.ByIsbn(bookIsbn))
            .mapLeft { LoanCreatorError.UnknownError(it) }
            .flatMap { books -> books.firstOrElse(predicate = { it.isAvailable }) { LoanCreatorError.BookNotAvailable(bookIsbn) } }
            .map { book -> withBook(book) }

    private fun LoanCreatorContext.createLoan(loanId: LoanId, createdAt: CreatedAt): LoanCreatorContext =
        withLoan(Loan.create(id = loanId, bookId = book.id, accountId = account.id, createdAt = createdAt))
            .withBook(book.unavailable())
            .withAccount(account.increaseLoans())

    private fun LoanCreatorContext.save() =
        loanRepository.saveOrElse(loan) { LoanCreatorError.UnknownError(it) }
            .flatMap { accountRepository.saveOrElse(account) { LoanCreatorError.UnknownError(it) } }
            .flatMap { bookRepository.saveOrElse(book) { LoanCreatorError.UnknownError(it) } }
            .map { }

    data class LoanCreatorContext(private val map: Map<String, Any> = mapOf()) {
        val loan: Loan
            get() = map["loan"]!! as Loan
        val book: Book
            get() = map["book"]!! as Book
        val account: Account
            get() = map["account"]!! as Account

        fun withLoan(loan: Loan): LoanCreatorContext =
            LoanCreatorContext(map + mutableMapOf("loan" to loan))

        fun withBook(book: Book): LoanCreatorContext =
            LoanCreatorContext(map + mutableMapOf("book" to book))

        fun withAccount(account: Account): LoanCreatorContext =
            LoanCreatorContext(map + mutableMapOf("account" to account))
    }
}

sealed class LoanCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class BookNotAvailable(bookIsbn: BookIsbn) : LoanCreatorError("book with isbn ${bookIsbn.value} is not available")

    class UserNotFound(accountId: AccountId) : LoanCreatorError("user ${accountId.value} not found")
    class UserHasTooManyLoans(accountId: AccountId) : LoanCreatorError("user ${accountId.value} has too many loans")
    class UnknownError(error: Throwable) : LoanCreatorError(error)
}

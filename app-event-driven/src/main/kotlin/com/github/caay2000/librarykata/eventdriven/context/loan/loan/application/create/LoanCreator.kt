package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.create

import arrow.core.Either
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.CreatedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository

class LoanCreator(
    private val loanRepository: LoanRepository,
) {
    fun invoke(
        loanId: LoanId,
        accountId: AccountId,
        bookIsbn: BookIsbn,
        createdAt: CreatedAt,
    ): Either<LoanCreatorError, Unit> = TODO()
//        guardAccountCurrentLoans(accountId)
//            .flatMap { ctx -> ctx.guardBookAvailability(bookIsbn) }
//            .map { ctx -> ctx.createLoan(loanId = loanId, createdAt = createdAt) }
//            .flatMap { ctx -> ctx.save() }

//    private fun guardAccountCurrentLoans(accountId: AccountId): Either<LoanCreatorError, LoanCreatorContext> =
//        accountRepository.findOrElse(
//            criteria = FindAccountCriteria.ById(accountId),
//            onResourceDoesNotExist = { LoanCreatorError.AccountNotFound(accountId) },
//        ).flatMap { account ->
//            if (account.hasReachedLoanLimit()) {
//                LoanCreatorError.AccountHasTooManyLoans(account.id).left()
//            } else {
//                LoanCreatorContext().withAccount(account).right()
//            }
//        }
//
//    private fun LoanCreatorContext.guardBookAvailability(bookIsbn: BookIsbn): Either<LoanCreatorError, LoanCreatorContext> {
//        val books = bookRepository.search(SearchBookCriteria.ByIsbn(bookIsbn))
//        return when {
//            books.isEmpty() -> LoanCreatorError.BookNotFound(bookIsbn).left()
//            books.none { it.isAvailable } -> LoanCreatorError.BookNotAvailable(bookIsbn).left()
//            else -> withBook(books.first { it.isAvailable }).right()
//        }
//    }
//
//    private fun LoanCreatorContext.createLoan(
//        loanId: LoanId,
//        createdAt: CreatedAt,
//    ): LoanCreatorContext =
//        withLoan(Loan.create(id = loanId, bookId = book.id, accountId = account.id, createdAt = createdAt))
//            .withBook(book.unavailable())
//            .withAccount(account.increaseLoans())
//
//    private fun LoanCreatorContext.save() =
//        loanRepository.saveOrElse(loan) { throw it }
//            .flatMap { accountRepository.saveOrElse(account) { throw it } }
//            .flatMap { bookRepository.saveOrElse(book) { throw it } }
//            .map { }
//
//    data class LoanCreatorContext(private val map: Map<String, Any> = mapOf()) {
//        val loan: Loan
//            get() = map["loan"]!! as Loan
//        val book: Book
//            get() = map["book"]!! as Book
//        val account: Account
//            get() = map["account"]!! as Account
//
//        fun withLoan(loan: Loan): LoanCreatorContext = LoanCreatorContext(map + mutableMapOf("loan" to loan))
//
//        fun withBook(book: Book): LoanCreatorContext = LoanCreatorContext(map + mutableMapOf("book" to book))
//
//        fun withAccount(account: Account): LoanCreatorContext = LoanCreatorContext(map + mutableMapOf("account" to account))
//    }
}

sealed class LoanCreatorError(message: String) : RuntimeException(message) {
    class BookNotAvailable(bookIsbn: BookIsbn) : LoanCreatorError("Book with isbn ${bookIsbn.value} is not available")

    class BookNotFound(bookIsbn: BookIsbn) : LoanCreatorError("Book with isbn ${bookIsbn.value} not found")

    class AccountNotFound(accountId: AccountId) : LoanCreatorError("Account ${accountId.value} not found")

    class AccountHasTooManyLoans(accountId: AccountId) : LoanCreatorError("Account ${accountId.value} has too many loans")
}

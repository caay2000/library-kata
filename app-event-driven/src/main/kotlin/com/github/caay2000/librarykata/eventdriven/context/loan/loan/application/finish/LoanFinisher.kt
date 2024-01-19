package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.finish

import arrow.core.Either
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.FinishedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository

class LoanFinisher(
    private val loanRepository: LoanRepository,
) {
    fun invoke(
        bookId: BookId,
        finishedAt: FinishedAt,
    ): Either<LoanFinisherError, Unit> = TODO()
//        findLoan(bookId)
//            .flatMap { ctx -> ctx.findBook(bookId) }
//            .flatMap { ctx -> ctx.findAccount(ctx.loan.accountId) }
//            .map { ctx -> ctx.finishLoan(finishedAt) }
//            .map { ctx -> ctx.save() }
//
//    private fun findLoan(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
//        loanRepository.findOrElse(
//            criteria = FindLoanCriteria.ByBookIdAndNotFinished(bookId),
//            onResourceDoesNotExist = { LoanFinisherError.LoanNotFound(bookId) },
//        ).map { LoanFinisherContext().withLoan(it) }
//
//    private fun LoanFinisherContext.findBook(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
//        bookRepository.findOrElse(
//            criteria = FindBookCriteria.ById(bookId),
//            onResourceDoesNotExist = { throw it },
//        ).map { book -> withBook(book) }
//
//    private fun LoanFinisherContext.findAccount(accountId: AccountId): Either<LoanFinisherError, LoanFinisherContext> =
//        accountRepository.findOrElse(
//            criteria = FindAccountCriteria.ById(accountId),
//            onResourceDoesNotExist = { throw it },
//        ).map { account -> withAccount(account) }
//
//    private fun LoanFinisherContext.finishLoan(finishedAt: FinishedAt): LoanFinisherContext =
//        withLoan(loan.finishLoan(finishedAt))
//            .withAccount(account.decreaseLoans())
//            .withBook(book.available())
//
//    private fun LoanFinisherContext.save() {
//        loanRepository.save(loan)
//        accountRepository.save(account)
//        bookRepository.save(book)
//    }
//
//    data class LoanFinisherContext(private val map: Map<String, Any> = mapOf()) {
//        val loan: Loan
//            get() = map["loan"]!! as Loan
//        val book: Book
//            get() = map["book"]!! as Book
//        val account: Account
//            get() = map["account"]!! as Account
//
//        fun withLoan(loan: Loan): LoanFinisherContext = LoanFinisherContext(map + mutableMapOf("loan" to loan))
//
//        fun withBook(book: Book): LoanFinisherContext = LoanFinisherContext(map + mutableMapOf("book" to book))
//
//        fun withAccount(account: Account): LoanFinisherContext = LoanFinisherContext(map + mutableMapOf("account" to account))
//    }
}

sealed class LoanFinisherError(message: String) : RuntimeException(message) {
    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
}

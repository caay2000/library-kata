package com.github.caay2000.librarykata.hexagonal.context.application.loan.finish

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.findOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.book.findOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.findOrElse

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
            .flatMap { ctx -> ctx.findBook(bookId) }
            .flatMap { ctx -> ctx.findAccount(ctx.loan.accountId) }
            .map { ctx -> ctx.finishLoan(finishedAt) }
            .map { ctx -> ctx.save() }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
        loanRepository.findOrElse(
            criteria = FindLoanCriteria.ByBookIdAndNotFinished(bookId),
            onResourceDoesNotExist = { LoanFinisherError.LoanNotFound(bookId) },
        ).map { LoanFinisherContext().withLoan(it) }

    private fun LoanFinisherContext.findBook(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
        bookRepository.findOrElse(
            criteria = FindBookCriteria.ById(bookId),
            onResourceDoesNotExist = { throw it },
        ).map { book -> withBook(book) }

    private fun LoanFinisherContext.findAccount(accountId: AccountId): Either<LoanFinisherError, LoanFinisherContext> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ById(accountId),
            onResourceDoesNotExist = { throw it },
        ).map { account -> withAccount(account) }

    private fun LoanFinisherContext.finishLoan(finishedAt: FinishedAt): LoanFinisherContext =
        withLoan(loan.finishLoan(finishedAt))
            .withAccount(account.decreaseLoans())
            .withBook(book.available())

    private fun LoanFinisherContext.save() {
        loanRepository.save(loan)
        accountRepository.save(account)
        bookRepository.save(book)
    }

    data class LoanFinisherContext(private val map: Map<String, Any> = mapOf()) {
        val loan: Loan
            get() = map["loan"]!! as Loan
        val book: Book
            get() = map["book"]!! as Book
        val account: Account
            get() = map["account"]!! as Account

        fun withLoan(loan: Loan): LoanFinisherContext = LoanFinisherContext(map + mutableMapOf("loan" to loan))

        fun withBook(book: Book): LoanFinisherContext = LoanFinisherContext(map + mutableMapOf("book" to book))

        fun withAccount(account: Account): LoanFinisherContext = LoanFinisherContext(map + mutableMapOf("account" to account))
    }
}

sealed class LoanFinisherError(message: String) : RuntimeException(message) {
    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
}

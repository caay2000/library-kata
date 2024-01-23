package com.github.caay2000.librarykata.hexagonal.context.loan.application.finish

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.account.domain.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.book.domain.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository

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
        loanRepository.find(FindLoanCriteria.ByBookIdAndNotFinished(bookId))
            ?.let { LoanFinisherContext().withLoan(it).right() }
            ?: LoanFinisherError.LoanNotFound(bookId).left()

    private fun LoanFinisherContext.findBook(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            ?.let { withBook(it).right() }
            ?: LoanFinisherError.BookNotFound(bookId).left()

    private fun LoanFinisherContext.findAccount(accountId: AccountId): Either<LoanFinisherError, LoanFinisherContext> =
        accountRepository.find(FindAccountCriteria.ById(accountId))
            ?.let { withAccount(it).right() }
            ?: LoanFinisherError.AccountNotFound(accountId).left()

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

    class BookNotFound(bookId: BookId) : LoanFinisherError("book ${bookId.value} not found")

    class AccountNotFound(accountId: AccountId) : LoanFinisherError("account ${accountId.value} not found")
}

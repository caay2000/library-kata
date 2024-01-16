package com.github.caay2000.librarykata.hexagonal.context.application.loan.finish

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.findOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.saveOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.book.saveOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.findOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.saveOrElse

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
            .flatMap { ctx -> ctx.save() }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
        loanRepository.findOrElse(
            criteria = FindLoanCriteria.ByBookIdAndNotFinished(bookId),
            onResourceDoesNotExist = { LoanFinisherError.LoanNotFound(bookId) },
            onUnexpectedError = { LoanFinisherError.UnknownError(it) },
        ).map { LoanFinisherContext().withLoan(it) }

    private fun LoanFinisherContext.findBook(bookId: BookId): Either<LoanFinisherError, LoanFinisherContext> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            .map { book -> withBook(book) }
            .mapLeft { LoanFinisherError.UnknownError(it) }

    private fun LoanFinisherContext.findAccount(accountId: AccountId): Either<LoanFinisherError, LoanFinisherContext> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ById(accountId),
            onUnexpectedError = { LoanFinisherError.UnknownError(it) },
        ).map { account -> withAccount(account) }

    private fun LoanFinisherContext.finishLoan(finishedAt: FinishedAt) =
        withLoan(loan.finishLoan(finishedAt))
            .withAccount(account.decreaseLoans())
            .withBook(book.available())

    private fun LoanFinisherContext.save() =
        loanRepository.saveOrElse(loan) { LoanFinisherError.UnknownError(it) }
            .flatMap { accountRepository.saveOrElse(account) { LoanFinisherError.UnknownError(it) } }
            .flatMap { bookRepository.saveOrElse(book) { LoanFinisherError.UnknownError(it) } }
            .map { }

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

sealed class LoanFinisherError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")

    class UnknownError(error: Throwable) : LoanFinisherError(error)
}

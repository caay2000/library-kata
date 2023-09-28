package com.github.caay2000.librarykata.hexagonal.context.application.loan.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.SearchBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
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
        checkAccount(accountId)
            .flatMap { checkBookAvailability(bookIsbn) }
            .map { book -> Loan.create(id = loanId, bookId = book.id, accountId = accountId, createdAt = createdAt) }
            .flatMap { loan -> loan.save() }

    private fun checkBookAvailability(bookIsbn: BookIsbn): Either<LoanCreatorError, Book> =
        searchAllBooksByIsbn(bookIsbn)
            .flatMap { books -> books.getFirstAvailableBook(bookIsbn) }

    private fun searchAllBooksByIsbn(bookIsbn: BookIsbn): Either<LoanCreatorError, List<Book>> =
        bookRepository.search(SearchBookCriteria.ByIsbn(bookIsbn))
            .mapLeft { LoanCreatorError.UnknownError(it) }

    private fun List<Book>.getFirstAvailableBook(isbn: BookIsbn): Either<LoanCreatorError, Book> =
        Either.catch { first { it.isAvailable } }
            .mapLeft { LoanCreatorError.BookNotAvailable(isbn) }

    private fun checkAccount(accountId: AccountId): Either<LoanCreatorError, Unit> =
        findUser(accountId)
            .flatMap { book -> book.checkCurrentLoans() }

    private fun findUser(accountId: AccountId): Either<LoanCreatorError, Account> =
        accountRepository.find(FindAccountCriteria.ById(accountId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanCreatorError.UserNotFound(accountId)
                    else -> LoanCreatorError.UnknownError(error)
                }
            }

    private fun Account.checkCurrentLoans(): Either<LoanCreatorError, Unit> =
        when {
            hasReachedLoanLimit() -> LoanCreatorError.UserHasTooManyLoans(id).left()
            else -> Unit.right()
        }

    private fun Loan.save(): Either<LoanCreatorError, Unit> =
        loanRepository.save(this)
            .mapLeft { LoanCreatorError.UnknownError(it) }
}

sealed class LoanCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class BookNotAvailable(bookIsbn: BookIsbn) : LoanCreatorError("book with isbn ${bookIsbn.value} is not available")

    class UserNotFound(accountId: AccountId) : LoanCreatorError("user ${accountId.value} not found")
    class UserHasTooManyLoans(accountId: AccountId) : LoanCreatorError("user ${accountId.value} has too many loans")
    class UnknownError(error: Throwable) : LoanCreatorError(error)
}

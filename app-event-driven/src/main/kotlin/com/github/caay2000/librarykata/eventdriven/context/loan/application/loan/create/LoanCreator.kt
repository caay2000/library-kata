package com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.loan.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.application.UserRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.CreatedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.User
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.UserId

class LoanCreator(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val loanRepository: LoanRepository,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(
        loanId: LoanId,
        userId: UserId,
        bookIsbn: BookIsbn,
        createdAt: CreatedAt,
    ): Either<LoanCreatorError, Unit> =
        checkUser(userId)
            .flatMap { checkBookAvailability(bookIsbn) }
            .map { book -> Loan.create(id = loanId, bookId = book.id, userId = userId, createdAt = createdAt) }
            .flatMap { loan -> loan.save() }
            .flatMap { loan -> loan.publishEvents() }

    private fun checkBookAvailability(bookIsbn: BookIsbn): Either<LoanCreatorError, Book> =
        searchAllBooksByIsbn(bookIsbn)
            .flatMap { books -> books.getFirstAvailableBook(bookIsbn) }

    private fun searchAllBooksByIsbn(bookIsbn: BookIsbn): Either<LoanCreatorError, List<Book>> =
        bookRepository.searchByIsbn(bookIsbn)
            .mapLeft { LoanCreatorError.UnknownError(it) }

    private fun List<Book>.getFirstAvailableBook(isbn: BookIsbn): Either<LoanCreatorError, Book> =
        Either.catch { first { it.isAvailable } }
            .mapLeft { LoanCreatorError.BookNotAvailable(isbn) }

    private fun checkUser(userId: UserId): Either<LoanCreatorError, Unit> =
        findUser(userId)
            .flatMap { book -> book.checkCurrentLoans() }

    private fun findUser(userId: UserId): Either<LoanCreatorError, User> =
        userRepository.findById(userId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanCreatorError.UserNotFound(userId)
                    else -> LoanCreatorError.UnknownError(error)
                }
            }

    private fun User.checkCurrentLoans(): Either<LoanCreatorError, Unit> =
        when {
            hasReachedLoanLimit() -> com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.create.LoanCreatorError.UserHasTooManyLoans(id).left()
            else -> Unit.right()
        }

    private fun Loan.save(): Either<LoanCreatorError, Loan> =
        loanRepository.save(this)
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.create.LoanCreatorError.UnknownError(it) }
            .map { this }

    private fun Loan.publishEvents(): Either<LoanCreatorError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.loan.application.loan.create.LoanCreatorError.UnknownError(it) }
}

sealed class LoanCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class BookNotAvailable(bookIsbn: BookIsbn) : LoanCreatorError("book with isbn ${bookIsbn.value} is not available")

    class UserNotFound(userId: UserId) : LoanCreatorError("user ${userId.value} not found")
    class UserHasTooManyLoans(userId: UserId) : LoanCreatorError("user ${userId.value} has too many loans")
    class UnknownError(error: Throwable) : LoanCreatorError(error)
}

package com.github.caay2000.projectskeleton.context.loan.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.loan.application.BookRepository
import com.github.caay2000.projectskeleton.context.loan.application.LoanRepository
import com.github.caay2000.projectskeleton.context.loan.application.UserRepository
import com.github.caay2000.projectskeleton.context.loan.domain.Book
import com.github.caay2000.projectskeleton.context.loan.domain.BookId
import com.github.caay2000.projectskeleton.context.loan.domain.CreatedAt
import com.github.caay2000.projectskeleton.context.loan.domain.Loan
import com.github.caay2000.projectskeleton.context.loan.domain.LoanId
import com.github.caay2000.projectskeleton.context.loan.domain.User
import com.github.caay2000.projectskeleton.context.loan.domain.UserId

class LoanCreator(
    private val bookRepository: BookRepository,
    private val userRepository: UserRepository,
    private val loanRepository: LoanRepository,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(
        loanId: LoanId,
        userId: UserId,
        bookId: BookId,
        createdAt: CreatedAt,
    ): Either<LoanCreatorError, Unit> =
        checkBook(bookId)
            .flatMap { checkUser(userId) }
            .map { Loan.create(id = loanId, bookId = bookId, userId = userId, createdAt = createdAt) }
            .flatMap { loan -> loan.save() }
            .flatMap { loan -> loan.publishEvents() }

    private fun checkBook(bookId: BookId): Either<LoanCreatorError, Unit> =
        findBook(bookId)
            .map { book -> book.checkAvailability() }

    private fun findBook(bookId: BookId): Either<LoanCreatorError, Book> =
        bookRepository.findById(bookId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanCreatorError.BookNotFound(bookId)
                    else -> LoanCreatorError.UnknownError(error)
                }
            }

    private fun Book.checkAvailability(): Either<LoanCreatorError, Unit> =
        if (isAvailable) {
            Unit.right()
        } else {
            LoanCreatorError.BookNotAvailable(id).left()
        }

    private fun checkUser(userId: UserId): Either<LoanCreatorError, Unit> =
        findUser(userId)
            .map { book -> book.checkCurrentLoans() }

    private fun findUser(userId: UserId): Either<LoanCreatorError, User> =
        userRepository.findById(userId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanCreatorError.UserNotFound(userId)
                    else -> LoanCreatorError.UnknownError(error)
                }
            }

    private fun User.checkCurrentLoans(): Either<LoanCreatorError, Unit> =
        if (currentLoans < 5) {
            Unit.right()
        } else {
            LoanCreatorError.UserHasTooManyLoans(id).left()
        }

    private fun Loan.save(): Either<LoanCreatorError, Loan> =
        loanRepository.save(this)
            .mapLeft { LoanCreatorError.UnknownError(it) }
            .map { this }

    private fun Loan.publishEvents(): Either<LoanCreatorError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { LoanCreatorError.UnknownError(it) }
}

sealed class LoanCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class BookNotFound(bookId: BookId) : LoanCreatorError("book $bookId not found")
    class BookNotAvailable(bookId: BookId) : LoanCreatorError("book $bookId is not available")

    class UserNotFound(userId: UserId) : LoanCreatorError("user $userId not found")
    class UserHasTooManyLoans(userId: UserId) : LoanCreatorError("user $userId has too many loans")
    class UnknownError(error: Throwable) : LoanCreatorError(error)
}

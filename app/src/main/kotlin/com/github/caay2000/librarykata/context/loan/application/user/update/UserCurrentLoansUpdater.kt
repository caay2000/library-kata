package com.github.caay2000.librarykata.context.loan.application.user.update

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.loan.application.UserRepository
import com.github.caay2000.librarykata.context.loan.domain.User
import com.github.caay2000.librarykata.context.loan.domain.UserId

class UserCurrentLoansUpdater(private val userRepository: UserRepository) {

    fun invoke(userId: UserId, value: Int): Either<UserLoansUpdaterError, Unit> =
        findUser(userId)
            .map { user -> user.updateCurrentLoans(value) }
            .flatMap { user -> user.save() }

    private fun findUser(userId: UserId): Either<UserLoansUpdaterError, User> =
        userRepository.findById(userId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> UserLoansUpdaterError.UserNotFound(userId)
                    else -> UserLoansUpdaterError.UnknownError(error)
                }
            }

    private fun User.save(): Either<UserLoansUpdaterError, Unit> =
        userRepository.save(this)
            .mapLeft { UserLoansUpdaterError.UnknownError(it) }
}

sealed class UserLoansUpdaterError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : UserLoansUpdaterError(error)
    class UserNotFound(userId: UserId) : UserLoansUpdaterError("user ${userId.value} not found")
}

package com.github.caay2000.librarykata.event.context.loan.application.user.update

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.loan.application.UserRepository
import com.github.caay2000.librarykata.event.context.loan.domain.User
import com.github.caay2000.librarykata.event.context.loan.domain.UserId

class UserCurrentLoansUpdater(private val userRepository: UserRepository) {
    fun invoke(
        userId: UserId,
        value: Int,
    ): Either<UserLoansUpdaterError, Unit> =
        findUser(userId)
            .map { user -> user.updateCurrentLoans(value) }
            .map { user -> user.save() }

    private fun findUser(userId: UserId): Either<UserLoansUpdaterError, User> =
        userRepository.find(userId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> UserLoansUpdaterError.UserNotFound(userId)
                    else -> throw error
                }
            }

    private fun User.save(): User = userRepository.save(this)
}

sealed class UserLoansUpdaterError(message: String) : RuntimeException(message) {
    class UserNotFound(userId: UserId) : UserLoansUpdaterError("user ${userId.value} not found")
}

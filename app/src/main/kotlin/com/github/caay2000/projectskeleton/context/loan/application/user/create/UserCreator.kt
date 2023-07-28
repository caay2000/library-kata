package com.github.caay2000.projectskeleton.context.loan.application.user.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.projectskeleton.context.loan.application.UserRepository
import com.github.caay2000.projectskeleton.context.loan.domain.User
import com.github.caay2000.projectskeleton.context.loan.domain.UserId

class UserCreator(private val userRepository: UserRepository) {

    fun invoke(userId: UserId): Either<UserCreatorError, Unit> =
        createUser(userId)
            .flatMap { user -> user.save() }

    private fun createUser(userId: UserId): Either<UserCreatorError, User> = User.create(userId).right()

    private fun User.save(): Either<UserCreatorError, Unit> =
        userRepository.save(this)
            .mapLeft { UserCreatorError.UnknownError(it) }
}

sealed class UserCreatorError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : UserCreatorError(error)
}

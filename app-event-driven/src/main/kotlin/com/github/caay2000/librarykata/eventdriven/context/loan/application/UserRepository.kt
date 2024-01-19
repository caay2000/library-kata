package com.github.caay2000.librarykata.eventdriven.context.loan.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.User
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.UserId

interface UserRepository {
    fun save(user: User): User

    fun find(id: UserId): Either<RepositoryError, User>
}

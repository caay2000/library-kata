package com.github.caay2000.projectskeleton.context.loan.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.loan.domain.User
import com.github.caay2000.projectskeleton.context.loan.domain.UserId

interface UserRepository {

    fun save(user: User): Either<RepositoryError, Unit>

    fun findById(id: UserId): Either<RepositoryError, User>
}

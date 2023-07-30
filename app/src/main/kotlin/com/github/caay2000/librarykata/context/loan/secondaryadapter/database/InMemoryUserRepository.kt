package com.github.caay2000.librarykata.context.loan.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.loan.application.UserRepository
import com.github.caay2000.librarykata.context.loan.domain.User
import com.github.caay2000.librarykata.context.loan.domain.UserId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryUserRepository(private val datasource: InMemoryDatasource) : UserRepository {

    companion object {
        private const val TABLE_NAME = "loan.user"
    }

    override fun save(user: User): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, user.id.toString(), user) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun findById(id: UserId): Either<RepositoryError, User> =
        Either.catch { datasource.getById<User>(TABLE_NAME, id.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(error)
                }
            }
}

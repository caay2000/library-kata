package com.github.caay2000.librarykata.event.context.loan.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.loan.application.UserRepository
import com.github.caay2000.librarykata.event.context.loan.domain.User
import com.github.caay2000.librarykata.event.context.loan.domain.UserId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryUserRepository(private val datasource: InMemoryDatasource) : UserRepository {
    companion object {
        private const val TABLE_NAME = "loan.user"
    }

    override fun save(user: User) = datasource.save(TABLE_NAME, user.id.toString(), user)

    override fun find(id: UserId): Either<RepositoryError, User> =
        Either.catch { datasource.getById<User>(TABLE_NAME, id.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> throw error
                }
            }
}

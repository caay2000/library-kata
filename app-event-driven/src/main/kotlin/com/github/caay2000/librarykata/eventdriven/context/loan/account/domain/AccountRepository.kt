package com.github.caay2000.librarykata.eventdriven.context.loan.account.domain

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError

interface AccountRepository {
    fun save(account: Account): Account

    // TODO handle not found error
    fun find(accountId: AccountId): Either<RepositoryError, Account>
}

fun <E> AccountRepository.findOrElse(
    id: AccountId,
    onResourceDoesNotExist: (Throwable) -> E = { throw it },
    onUnexpectedError: (Throwable) -> E = { throw it },
): Either<E, Account> =
    find(id).mapLeft { error ->
        if (error is RepositoryError.NotFoundError) {
            onResourceDoesNotExist(error)
        } else {
            onUnexpectedError(error)
        }
    }

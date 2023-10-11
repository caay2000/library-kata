package com.github.caay2000.librarykata.hexagonalarrow.context.application.account

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Account
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Email
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.PhonePrefix

interface AccountRepository : Repository {

    fun save(account: Account): Either<RepositoryError, Unit>
    fun find(criteria: FindAccountCriteria): Either<RepositoryError, Account>
}

sealed class FindAccountCriteria {
    class ById(val id: AccountId) : FindAccountCriteria()
    class ByIdentityNumber(val identityNumber: IdentityNumber) : FindAccountCriteria()
    class ByEmail(val email: Email) : FindAccountCriteria()
    class ByPhone(val phonePrefix: PhonePrefix, val phoneNumber: PhoneNumber) : FindAccountCriteria()
}

fun <E> AccountRepository.saveOrElse(account: Account, onError: (Throwable) -> E): Either<E, Account> =
    save(account).mapLeft { onError(it) }.map { account }

fun <E> AccountRepository.findOrElse(
    criteria: FindAccountCriteria,
    onUnexpectedError: (Throwable) -> E,
    onResourceDoesNotExist: (Throwable) -> E = { onUnexpectedError(it) },
): Either<E, Account> =
    find(criteria).mapLeft { error ->
        if (error is RepositoryError.NotFoundError) {
            onResourceDoesNotExist(error)
        } else {
            onUnexpectedError(error)
        }
    }

package com.github.caay2000.librarykata.eventdriven.context.account.domain

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError

interface AccountRepository : Repository {
    fun save(account: Account): Account

    fun find(criteria: FindAccountCriteria): Either<RepositoryError, Account>

    fun search(criteria: SearchAccountCriteria): List<Account>
}

sealed class FindAccountCriteria {
    class ById(val id: AccountId) : FindAccountCriteria()

    class ByIdentityNumber(val identityNumber: IdentityNumber) : FindAccountCriteria()

    class ByEmail(val email: Email) : FindAccountCriteria()

    class ByPhone(val phonePrefix: PhonePrefix, val phoneNumber: PhoneNumber) : FindAccountCriteria()
}

sealed class SearchAccountCriteria {
    data object All : SearchAccountCriteria()

    data class ByPhoneNumber(val phoneNumber: PhoneNumber) : SearchAccountCriteria()

    data class ByEmail(val email: Email) : SearchAccountCriteria()
}

fun <E> AccountRepository.findOrElse(
    criteria: FindAccountCriteria,
    onResourceDoesNotExist: (Throwable) -> E = { throw it },
    onUnexpectedError: (Throwable) -> E = { throw it },
): Either<E, Account> =
    find(criteria).mapLeft { error ->
        if (error is RepositoryError.NotFoundError) {
            onResourceDoesNotExist(error)
        } else {
            onUnexpectedError(error)
        }
    }

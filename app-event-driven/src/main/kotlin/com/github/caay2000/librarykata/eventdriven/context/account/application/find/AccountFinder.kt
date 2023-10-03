package com.github.caay2000.librarykata.eventdriven.context.account.application.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.account.application.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.application.FindAccountCriteria
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId

class AccountFinder(private val accountRepository: AccountRepository) {

    fun invoke(accountId: AccountId): Either<AccountFinderError, Account> =
        accountRepository.findBy(FindAccountCriteria.ById(accountId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> AccountFinderError.AccountNotFoundError(accountId)
                    else -> AccountFinderError.Unknown(error)
                }
            }
}

sealed class AccountFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountFinderError(error)
    class AccountNotFoundError(accountId: AccountId) : AccountFinderError("account $accountId not found")
}

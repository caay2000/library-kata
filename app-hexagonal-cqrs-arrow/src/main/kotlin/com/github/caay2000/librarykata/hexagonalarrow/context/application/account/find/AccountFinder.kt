package com.github.caay2000.librarykata.hexagonalarrow.context.application.account.find

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonalarrow.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonalarrow.context.application.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonalarrow.context.application.account.findOrElse
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.Account
import com.github.caay2000.librarykata.hexagonalarrow.context.domain.AccountId

class AccountFinder(private val accountRepository: AccountRepository) {

    fun invoke(accountId: AccountId): Either<AccountFinderError, Account> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ById(accountId),
            onUnexpectedError = { AccountFinderError.Unknown(it) },
        ) { AccountFinderError.AccountNotFoundError(accountId) }
}

sealed class AccountFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountFinderError(error)
    class AccountNotFoundError(accountId: AccountId) : AccountFinderError("account $accountId not found")
}

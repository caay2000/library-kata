package com.github.caay2000.librarykata.hexagonal.context.application.account.find

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.findOrElse

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

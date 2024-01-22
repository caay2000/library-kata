package com.github.caay2000.librarykata.eventdriven.context.loan.account.application.find

import arrow.core.Either
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.findOrElse

class AccountFinder(private val accountRepository: AccountRepository) {
    fun invoke(accountId: AccountId): Either<AccountFinderError, Account> =
        accountRepository.findOrElse(
            id = accountId,
            onResourceDoesNotExist = { AccountFinderError.AccountNotFoundError(accountId) },
        )
}

sealed class AccountFinderError(message: String) : RuntimeException(message) {
    class AccountNotFoundError(accountId: AccountId) : AccountFinderError("Account ${accountId.value} not found")
}

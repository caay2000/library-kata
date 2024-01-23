package com.github.caay2000.librarykata.hexagonal.context.application.account.find

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria

class AccountFinder(private val accountRepository: AccountRepository) {
    fun invoke(accountId: AccountId): Either<AccountFinderError, Account> =
        accountRepository.find(FindAccountCriteria.ById(accountId))?.right()
            ?: AccountFinderError.AccountNotFoundError(accountId).left()
}

sealed class AccountFinderError(message: String) : RuntimeException(message) {
    class AccountNotFoundError(accountId: AccountId) : AccountFinderError("Account ${accountId.value} not found")
}

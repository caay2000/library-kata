package com.github.caay2000.librarykata.eventdriven.context.loan.application.account.update

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository

class AccountLoansUpdater(
    private val accountRepository: AccountRepository,
) {
    fun invoke(
        accountId: AccountId,
        type: UpdateType,
    ): Either<AccountLoansUpdaterError, Unit> =
        findAccount(accountId)
            .map { account -> account.update(type) }
            .map { account -> accountRepository.save(account) }

    private fun findAccount(accountId: AccountId) =
        accountRepository.find(accountId)
            ?.right()
            ?: AccountLoansUpdaterError.AccountNotFound(accountId).left()

    private fun Account.update(type: UpdateType) =
        when (type) {
            UpdateType.INCREASE -> increaseLoans()
            UpdateType.DECREASE -> decreaseLoans()
        }

    enum class UpdateType {
        INCREASE,
        DECREASE,
    }
}

sealed class AccountLoansUpdaterError(message: String) : RuntimeException(message) {
    class AccountNotFound(accountId: AccountId) : AccountLoansUpdaterError("account ${accountId.value} not found")
}

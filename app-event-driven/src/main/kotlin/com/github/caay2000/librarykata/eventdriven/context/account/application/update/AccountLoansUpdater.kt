package com.github.caay2000.librarykata.eventdriven.context.account.application.update

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.FindAccountCriteria

class AccountLoansUpdater(
    private val accountRepository: AccountRepository,
) {
    fun invoke(
        accountId: AccountId,
        type: UpdateType,
    ): Either<AccountLoansUpdaterError, Unit> =
        findAccount(accountId)
            ?.let { account ->
                accountRepository.save(account.update(type))
                Unit.right()
            }
            ?: AccountLoansUpdaterError.AccountNotFound().left()

    private fun findAccount(accountId: AccountId) = accountRepository.find(FindAccountCriteria.ById(accountId))

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
    class AccountNotFound : AccountLoansUpdaterError("account not found")
}

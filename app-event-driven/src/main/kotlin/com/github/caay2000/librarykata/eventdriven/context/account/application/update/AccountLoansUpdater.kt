package com.github.caay2000.librarykata.eventdriven.context.account.application.update

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
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
            .map { account -> account.update(type) }
            .map { account -> accountRepository.save(account) }

    private fun findAccount(accountId: AccountId) =
        accountRepository.find(FindAccountCriteria.ById(accountId))
            .mapLeft {
                if (it is RepositoryError.NotFoundError)
                    AccountLoansUpdaterError.AccountNotFound()
                else
                    throw it
            }

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

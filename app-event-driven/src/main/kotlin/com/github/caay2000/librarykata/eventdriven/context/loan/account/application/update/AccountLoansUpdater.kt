package com.github.caay2000.librarykata.eventdriven.context.loan.account.application.update

import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository

class AccountLoansUpdater(
    private val accountRepository: AccountRepository,
) {
    fun invoke(
        accountId: AccountId,
        type: UpdateType,
    ) = findAccount(accountId)
        .update(type)
        .save()

    private fun findAccount(accountId: AccountId) = accountRepository.find(accountId)

    private fun Account.update(type: UpdateType) =
        when (type) {
            UpdateType.INCREASE -> increaseLoans()
            UpdateType.DECREASE -> decreaseLoans()
        }

    private fun Account.save(): Unit = accountRepository.save(this).let { }

    enum class UpdateType {
        INCREASE,
        DECREASE,
    }
}

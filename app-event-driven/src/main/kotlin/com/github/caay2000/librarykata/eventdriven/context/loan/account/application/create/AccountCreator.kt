package com.github.caay2000.librarykata.eventdriven.context.loan.account.application.create

import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository

class AccountCreator(private val accountRepository: AccountRepository) {
    fun invoke(accountId: AccountId) {
        Account.create(accountId).save()
    }

    private fun Account.save(): Account = accountRepository.save(this)
}

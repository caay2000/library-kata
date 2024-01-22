package com.github.caay2000.librarykata.eventdriven.context.loan.application.account.create

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository

class AccountCreator(private val accountRepository: AccountRepository) {
    fun invoke(accountId: AccountId) {
        val account = Account.create(accountId)
        accountRepository.save(account)
    }
}

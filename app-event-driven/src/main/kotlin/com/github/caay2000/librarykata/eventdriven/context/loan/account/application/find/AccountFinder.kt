package com.github.caay2000.librarykata.eventdriven.context.loan.account.application.find

import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository

class AccountFinder(private val accountRepository: AccountRepository) {
    fun invoke(accountId: AccountId): Account = accountRepository.find(accountId)
}

package com.github.caay2000.librarykata.hexagonal.context.application.account.search

import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.SearchAccountCriteria

class AccountSearcher(private val accountRepository: AccountRepository) {
    fun invoke(criteria: SearchAccountCriteria): List<Account> = accountRepository.search(criteria)
}

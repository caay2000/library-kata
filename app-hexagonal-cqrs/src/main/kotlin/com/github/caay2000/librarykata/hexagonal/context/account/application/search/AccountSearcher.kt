package com.github.caay2000.librarykata.hexagonal.context.account.application.search

import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.account.domain.SearchAccountCriteria

class AccountSearcher(private val accountRepository: AccountRepository) {
    fun invoke(criteria: SearchAccountCriteria): List<Account> = accountRepository.search(criteria)
}

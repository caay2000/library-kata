package com.github.caay2000.librarykata.eventdriven.context.loan.domain

interface AccountRepository {
    fun save(account: Account): Account

    fun find(accountId: AccountId): Account?
}

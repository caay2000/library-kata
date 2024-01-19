package com.github.caay2000.librarykata.eventdriven.context.loan.account.domain

interface AccountRepository {
    fun save(account: Account): Account
}

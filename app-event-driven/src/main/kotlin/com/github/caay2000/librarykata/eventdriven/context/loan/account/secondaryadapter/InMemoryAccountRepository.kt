package com.github.caay2000.librarykata.eventdriven.context.loan.account.secondaryadapter

import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    override fun save(account: Account): Account = datasource.save(TABLE_NAME, account.id.toString(), account)

    companion object {
        private const val TABLE_NAME = "loan.account"
    }
}

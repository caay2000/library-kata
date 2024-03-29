package com.github.caay2000.librarykata.eventdriven.context.loan.secondaryadapter.database

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    override fun save(account: Account): Account = datasource.save(TABLE_NAME, account.id.value, account)

    override fun find(accountId: AccountId): Account? = datasource.getById<Account>(TABLE_NAME, accountId.value)

    companion object {
        private const val TABLE_NAME = "loan.account"
    }
}

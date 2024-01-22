package com.github.caay2000.librarykata.eventdriven.context.loan.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.database.mapRepositoryErrors
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    override fun save(account: Account): Account = datasource.save(TABLE_NAME, account.id.value, account)

    override fun find(accountId: AccountId): Either<RepositoryError, Account> =
        Either.catch { datasource.getById<Account>(TABLE_NAME, accountId.value)!! }
            .mapRepositoryErrors()

    companion object {
        private const val TABLE_NAME = "loan.account"
    }
}

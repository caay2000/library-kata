package com.github.caay2000.librarykata.event.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.account.application.AccountRepository
import com.github.caay2000.librarykata.event.context.account.application.FindAccountCriteria
import com.github.caay2000.librarykata.event.context.account.domain.Account
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    companion object {
        private const val TABLE_NAME = "account.account"
    }

    override fun save(account: Account): Account = datasource.save(TABLE_NAME, account.id.value, account)

    override fun search(): List<Account> = datasource.getAll(TABLE_NAME)

    override fun findBy(criteria: FindAccountCriteria): Either<RepositoryError, Account> =
        Either.catch {
            when (criteria) {
                is FindAccountCriteria.ById -> datasource.getById<Account>(TABLE_NAME, criteria.id.value)!!
                is FindAccountCriteria.ByIdentityNumber -> datasource.getAll<Account>(TABLE_NAME).first { it.identityNumber == criteria.identityNumber }
                is FindAccountCriteria.ByEmail -> datasource.getAll<Account>(TABLE_NAME).first { it.email == criteria.email }
                is FindAccountCriteria.ByPhone -> datasource.getAll<Account>(TABLE_NAME).first { it.phonePrefix == criteria.phonePrefix && it.phoneNumber == criteria.phoneNumber }
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> throw error
            }
        }
}

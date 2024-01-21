package com.github.caay2000.librarykata.eventdriven.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.FindAccountCriteria
import com.github.caay2000.librarykata.eventdriven.context.account.domain.SearchAccountCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    override fun save(account: Account) = datasource.save(TABLE_NAME, account.id.value, account)

    override fun find(criteria: FindAccountCriteria): Either<RepositoryError, Account> =
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

    override fun search(criteria: SearchAccountCriteria): List<Account> =
        when (criteria) {
            SearchAccountCriteria.All -> datasource.getAll(TABLE_NAME)
            is SearchAccountCriteria.ByPhoneNumber -> datasource.getAll<Account>(TABLE_NAME).filter { it.phoneNumber.value.contains(criteria.phoneNumber.value) }
            is SearchAccountCriteria.ByEmail -> datasource.getAll<Account>(TABLE_NAME).filter { it.email.value.contains(criteria.email.value) }
        }

    companion object {
        private const val TABLE_NAME = "account.account"
    }
}

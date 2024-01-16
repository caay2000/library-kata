package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.SearchAccountCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {
    override fun save(account: Account): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, account.id.toString(), account) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun find(criteria: FindAccountCriteria): Either<RepositoryError, Account> =
        Either.catch {
            when (criteria) {
                is FindAccountCriteria.ById -> datasource.getById<Account>(TABLE_NAME, criteria.id.toString())!!
                is FindAccountCriteria.ByIdentityNumber -> datasource.getAll<Account>(TABLE_NAME).first { it.identityNumber == criteria.identityNumber }
                is FindAccountCriteria.ByEmail -> datasource.getAll<Account>(TABLE_NAME).first { it.email == criteria.email }
                is FindAccountCriteria.ByPhone -> datasource.getAll<Account>(TABLE_NAME).first { it.phonePrefix == criteria.phonePrefix && it.phoneNumber == criteria.phoneNumber }
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> RepositoryError.Unknown(error)
            }
        }

    override fun search(criteria: SearchAccountCriteria): Either<RepositoryError, List<Account>> =
        Either.catch {
            when (criteria) {
                SearchAccountCriteria.All -> datasource.getAll(TABLE_NAME)
                is SearchAccountCriteria.ByPhoneNumber -> datasource.getAll<Account>(TABLE_NAME).filter { it.phoneNumber.value.contains(criteria.phoneNumber.value) }
                is SearchAccountCriteria.ByEmail -> datasource.getAll<Account>(TABLE_NAME).filter { it.email.value.contains(criteria.email.value) }
            }
        }.mapLeft { error -> RepositoryError.Unknown(error) }

    companion object {
        private const val TABLE_NAME = "account"
    }
}

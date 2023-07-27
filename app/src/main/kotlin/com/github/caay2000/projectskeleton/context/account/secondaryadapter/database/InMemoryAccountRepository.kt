package com.github.caay2000.projectskeleton.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.application.FindAccountCriteria
import com.github.caay2000.projectskeleton.context.account.domain.Account

class InMemoryAccountRepository(private val datasource: InMemoryDatasource) : AccountRepository {

    companion object {
        private const val TABLE_NAME = "account.account"
    }

    override fun save(account: Account): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, account.id.toString(), account) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun searchAll(): Either<RepositoryError, List<Account>> =
        Either.catch { datasource.getAll<Account>(TABLE_NAME) }
            .mapLeft { RepositoryError.Unknown(it) }

    override fun findBy(criteria: FindAccountCriteria): Either<RepositoryError, Account> =
        Either.catch {
            when (criteria) {
                is FindAccountCriteria.ByAccountNumber -> datasource.getById<Account>(TABLE_NAME, criteria.accountNumber.toString())!!
                is FindAccountCriteria.ByEmail -> datasource.getById<Account>(TABLE_NAME, criteria.email.toString())!!
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> RepositoryError.Unknown(error)
            }
        }
}

package com.github.caay2000.projectskeleton.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.memorydb.Datasource
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.Email

class InMemoryAccountRepository(private val datasource: Datasource) : AccountRepository {

    companion object {
        private const val TABLE_NAME = "account"
    }

    override fun save(account: Account): Either<Throwable, Unit> =
        Either.catch { datasource.save(TABLE_NAME, account.id.toString(), account) }

    override fun findAll(): Either<Throwable, List<Account>> =
        Either.catch { datasource.getAll(TABLE_NAME) }

    override fun findById(id: AccountId): Either<Throwable, Account> =
        Either.catch { datasource.getById<Account>(TABLE_NAME, id.toString())!! }

    override fun findByEmail(email: Email): Either<Throwable, Account> =
        Either.catch { datasource.getAll<Account>(TABLE_NAME).find { it.email == email }!! }
}

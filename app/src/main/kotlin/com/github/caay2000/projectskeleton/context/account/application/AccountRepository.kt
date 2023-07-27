package com.github.caay2000.projectskeleton.context.account.application

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.account.domain.Email

interface AccountRepository : Repository {

    fun save(account: Account): Either<RepositoryError, Unit>
    fun searchAll(): Either<RepositoryError, List<Account>>
    fun findBy(criteria: FindAccountCriteria): Either<RepositoryError, Account>
}

sealed class FindAccountCriteria {
    class ByAccountNumber(val accountNumber: AccountNumber) : FindAccountCriteria()
    class ByEmail(val email: Email) : FindAccountCriteria()
}

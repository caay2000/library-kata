package com.github.caay2000.librarykata.context.account.application

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.account.domain.Account
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.domain.Email
import com.github.caay2000.librarykata.context.account.domain.IdentityNumber
import com.github.caay2000.librarykata.context.account.domain.PhoneNumber
import com.github.caay2000.librarykata.context.account.domain.PhonePrefix

interface AccountRepository : Repository {

    fun save(account: Account): Either<RepositoryError, Unit>
    fun searchAll(): Either<RepositoryError, List<Account>>
    fun findBy(criteria: FindAccountCriteria): Either<RepositoryError, Account>
}

sealed class FindAccountCriteria {
    class ById(val id: AccountId) : FindAccountCriteria()
    class ByIdentityNumber(val identityNumber: IdentityNumber) : FindAccountCriteria()
    class ByEmail(val email: Email) : FindAccountCriteria()
    class ByPhone(val phonePrefix: PhonePrefix, val phoneNumber: PhoneNumber) : FindAccountCriteria()
}

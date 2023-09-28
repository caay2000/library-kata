package com.github.caay2000.librarykata.hexagonal.context.application.account

import arrow.core.Either
import com.github.caay2000.common.database.Repository
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.PhonePrefix

interface AccountRepository : Repository {

    fun save(account: Account): Either<RepositoryError, Unit>
    fun find(criteria: FindAccountCriteria): Either<RepositoryError, Account>
    fun search(criteria: SearchAccountCriteria): Either<RepositoryError, List<Account>>
}

sealed class FindAccountCriteria {
    class ById(val id: AccountId) : FindAccountCriteria()
    class ByIdentityNumber(val identityNumber: IdentityNumber) : FindAccountCriteria()
    class ByEmail(val email: Email) : FindAccountCriteria()
    class ByPhone(val phonePrefix: PhonePrefix, val phoneNumber: PhoneNumber) : FindAccountCriteria()
}

sealed class SearchAccountCriteria {
    data object All : SearchAccountCriteria()
}

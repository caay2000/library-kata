package com.github.caay2000.projectskeleton.context.account.application

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.Email

interface AccountRepository {

    fun save(account: Account): Either<Throwable, Unit>
    fun findAll(): Either<Throwable, List<Account>>
    fun findById(id: AccountId): Either<Throwable, Account>
    fun findByEmail(email: Email): Either<Throwable, Account>
}

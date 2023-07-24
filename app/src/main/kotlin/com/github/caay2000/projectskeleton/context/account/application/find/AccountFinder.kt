package com.github.caay2000.projectskeleton.context.account.application.find

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountId

class AccountFinder(private val accountRepository: AccountRepository) {

    fun invoke(accountId: AccountId): Either<AccountFinderError, Account> =
        accountRepository.findById(accountId)
            .mapLeft { error ->
                if (error is NullPointerException) {
                    AccountFinderError.AccountDoesNotExistsError(accountId)
                } else {
                    AccountFinderError.Unknown(error)
                }
            }
}

sealed class AccountFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountFinderError(error)
    class AccountDoesNotExistsError(id: AccountId) : AccountFinderError("account with id $id does not exists")
}

package com.github.caay2000.projectskeleton.context.account.application.find

import arrow.core.Either
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.application.FindAccountCriteria
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountNumber

class AccountFinder(private val accountRepository: AccountRepository) {

    fun invoke(accountNumber: AccountNumber): Either<AccountFinderError, Account> =
        accountRepository.findBy(FindAccountCriteria.ByAccountNumber(accountNumber))
            .mapLeft { error ->
                if (error is NullPointerException) {
                    AccountFinderError.AccountDoesNotExistsError(accountNumber)
                } else {
                    AccountFinderError.Unknown(error)
                }
            }
}

sealed class AccountFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountFinderError(error)
    class AccountDoesNotExistsError(accountNumber: AccountNumber) : AccountFinderError("account $accountNumber does not exists")
}

package com.github.caay2000.librarykata.hexagonal.context.application.account.search

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.SearchAccountCriteria

class AccountSearcher(private val accountRepository: AccountRepository) {

    fun invoke(criteria: SearchAccountCriteria): Either<AccountSearcherError, List<Account>> =
        accountRepository.search(criteria)
            .mapLeft { AccountSearcherError.Unknown(it) }
}

sealed class AccountSearcherError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountSearcherError(error)
}

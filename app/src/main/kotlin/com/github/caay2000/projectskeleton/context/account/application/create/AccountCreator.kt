package com.github.caay2000.projectskeleton.context.account.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.CreateAccountRequest
import com.github.caay2000.projectskeleton.context.account.domain.Email

class AccountCreator(
    private val accountRepository: AccountRepository,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardEmailIsNotRepeated(request.email)
            .map { Account.create(request) }
            .flatMap { account -> account.save() }
            .flatMap { account -> account.publishEvents() }

    private fun guardEmailIsNotRepeated(email: Email): Either<AccountCreatorError, Unit> =
        accountRepository.findByEmail(email)
            .flatMap { AccountCreatorError.AccountAlreadyExists(email).left() }
            .recover { error ->
                when (error) {
                    is NullPointerException -> Unit.right()
                    is AccountCreatorError -> raise(error)
                    else -> raise(AccountCreatorError.Unknown(error))
                }
            }

    private fun Account.save(): Either<AccountCreatorError, Account> =
        accountRepository.save(this)
            .mapLeft { AccountCreatorError.Unknown(it) }
            .map { this }

    private fun Account.publishEvents(): Either<AccountCreatorError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { AccountCreatorError.Unknown(it) }
}

sealed class AccountCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountCreatorError(error)
    class AccountAlreadyExists(email: Email) : AccountCreatorError("account with email $email already exists")
}

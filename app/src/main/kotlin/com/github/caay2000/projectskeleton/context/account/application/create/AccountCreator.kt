package com.github.caay2000.projectskeleton.context.account.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.account.application.AccountRepository
import com.github.caay2000.projectskeleton.context.account.application.FindAccountCriteria
import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.CreateAccountRequest
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.IdentityNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix

class AccountCreator(
    private val accountRepository: AccountRepository,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardIdentityNumberIsNotRepeated(request.identityNumber)
            .flatMap { guardEmailIsNotRepeated(request.email) }
            .flatMap { guardPhoneIsNotRepeated(request.phonePrefix, request.phoneNumber) }
            .map { Account.create(request) }
            .flatMap { account -> account.save() }
            .flatMap { account -> account.publishEvents() }

    private fun guardIdentityNumberIsNotRepeated(identityNumber: IdentityNumber): Either<AccountCreatorError, Unit> =
        accountRepository.findBy(FindAccountCriteria.ByIdentityNumber(identityNumber))
            .flatMap { AccountCreatorError.IdentityNumberAlreadyExists(identityNumber).left() }
            .recover { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> Unit.right()
                    is AccountCreatorError -> raise(error)
                    else -> raise(AccountCreatorError.Unknown(error))
                }
            }

    private fun guardEmailIsNotRepeated(email: Email): Either<AccountCreatorError, Unit> =
        accountRepository.findBy(FindAccountCriteria.ByEmail(email))
            .flatMap { AccountCreatorError.EmailAlreadyExists(email).left() }
            .recover { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> Unit.right()
                    is AccountCreatorError -> raise(error)
                    else -> raise(AccountCreatorError.Unknown(error))
                }
            }

    private fun guardPhoneIsNotRepeated(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber): Either<AccountCreatorError, Unit> =
        accountRepository.findBy(FindAccountCriteria.ByPhone(phonePrefix, phoneNumber))
            .flatMap { AccountCreatorError.PhoneAlreadyExists(phonePrefix, phoneNumber).left() }
            .recover { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> Unit.right()
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
    class IdentityNumberAlreadyExists(identityNumber: IdentityNumber) : AccountCreatorError("an account with identity number ${identityNumber.value} already exists")
    class EmailAlreadyExists(email: Email) : AccountCreatorError("an account with email ${email.value} already exists")
    class PhoneAlreadyExists(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber) :
        AccountCreatorError("an account with phone ${phonePrefix.value} ${phoneNumber.value} already exists")
}

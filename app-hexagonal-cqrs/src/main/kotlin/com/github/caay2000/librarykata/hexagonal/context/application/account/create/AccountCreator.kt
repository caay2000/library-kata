package com.github.caay2000.librarykata.hexagonal.context.application.account.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.raise.Raise
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.application.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.CreateAccountRequest
import com.github.caay2000.librarykata.hexagonal.context.domain.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.PhonePrefix

class AccountCreator(private val accountRepository: AccountRepository) {

    fun invoke(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardAccountCanBeCreated(request)
            .map { Account.create(request) }
            .flatMap { account -> account.save() }

    private fun guardAccountCanBeCreated(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardIdentityNumberIsNotRepeated(request.identityNumber)
            .flatMap { guardEmailIsNotRepeated(request.email) }
            .flatMap { guardPhoneIsNotRepeated(request.phonePrefix, request.phoneNumber) }

    private fun guardIdentityNumberIsNotRepeated(identityNumber: IdentityNumber): Either<AccountCreatorError, Unit> =
        accountRepository.find(FindAccountCriteria.ByIdentityNumber(identityNumber))
            .flatMap { AccountCreatorError.IdentityNumberAlreadyExists(identityNumber).left() }
            .recover { error -> handleGuardErrors(error) }

    private fun guardEmailIsNotRepeated(email: Email): Either<AccountCreatorError, Unit> =
        accountRepository.find(FindAccountCriteria.ByEmail(email))
            .flatMap { AccountCreatorError.EmailAlreadyExists(email).left() }
            .recover { error -> handleGuardErrors(error) }

    private fun guardPhoneIsNotRepeated(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber): Either<AccountCreatorError, Unit> =
        accountRepository.find(FindAccountCriteria.ByPhone(phonePrefix, phoneNumber))
            .flatMap { AccountCreatorError.PhoneAlreadyExists(phonePrefix, phoneNumber).left() }
            .recover { error -> handleGuardErrors(error) }

    private fun Raise<AccountCreatorError>.handleGuardErrors(error: java.lang.RuntimeException) {
        when (error) {
            is RepositoryError.NotFoundError -> Unit.right()
            is AccountCreatorError -> raise(error)
            else -> raise(AccountCreatorError.Unknown(error))
        }
    }

    private fun Account.save(): Either<AccountCreatorError, Unit> =
        accountRepository.save(this)
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

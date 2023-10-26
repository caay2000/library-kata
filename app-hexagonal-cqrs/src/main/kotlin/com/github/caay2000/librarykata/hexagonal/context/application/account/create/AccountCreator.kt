package com.github.caay2000.librarykata.hexagonal.context.application.account.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.CreateAccountRequest
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.account.findOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.account.saveOrElse

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
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ByIdentityNumber(identityNumber),
            onResourceDoesNotExist = { AccountCreatorError.AccountNotFound() },
            onUnexpectedError = { AccountCreatorError.Unknown(it) },
        ).flatMap { AccountCreatorError.IdentityNumberAlreadyExists(identityNumber).left() }
            .validateAccountNotFound()

    private fun guardEmailIsNotRepeated(email: Email): Either<AccountCreatorError, Unit> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ByEmail(email),
            onResourceDoesNotExist = { AccountCreatorError.AccountNotFound() },
            onUnexpectedError = { AccountCreatorError.Unknown(it) },
        ).flatMap { AccountCreatorError.EmailAlreadyExists(email).left() }
            .validateAccountNotFound()

    private fun guardPhoneIsNotRepeated(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber): Either<AccountCreatorError, Unit> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ByPhone(phonePrefix, phoneNumber),
            onResourceDoesNotExist = { AccountCreatorError.AccountNotFound() },
            onUnexpectedError = { AccountCreatorError.Unknown(it) },
        ).flatMap { AccountCreatorError.PhoneAlreadyExists(phonePrefix, phoneNumber).left() }
            .validateAccountNotFound()

    private fun Either<AccountCreatorError, Unit>.validateAccountNotFound() =
        recover { error ->
            when (error) {
                is AccountCreatorError.AccountNotFound -> Unit.right()
                else -> raise(error)
            }
        }

    private fun Account.save(): Either<AccountCreatorError, Unit> =
        accountRepository.saveOrElse(this) { AccountCreatorError.Unknown(it) }.map {}
}

sealed class AccountCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : AccountCreatorError(error)
    class AccountNotFound : AccountCreatorError("account not foung")
    class IdentityNumberAlreadyExists(identityNumber: IdentityNumber) :
        AccountCreatorError("an account with identity number ${identityNumber.value} already exists")

    class EmailAlreadyExists(email: Email) : AccountCreatorError("an account with email ${email.value} already exists")
    class PhoneAlreadyExists(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber) :
        AccountCreatorError("an account with phone ${phonePrefix.value} ${phoneNumber.value} already exists")
}

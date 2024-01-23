package com.github.caay2000.librarykata.hexagonal.context.application.account.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.account.CreateAccountRequest
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.FindAccountCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.account.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Phone

class AccountCreator(private val accountRepository: AccountRepository) {
    fun invoke(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardAccountCanBeCreated(request)
            .map { Account.create(request) }
            .map { account -> account.save() }

    private fun guardAccountCanBeCreated(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardIdentityNumberIsNotRepeated(request.identityNumber)
            .flatMap { guardEmailIsNotRepeated(request.email) }
            .flatMap { guardPhoneIsNotRepeated(request.phone) }

    private fun guardIdentityNumberIsNotRepeated(identityNumber: IdentityNumber): Either<AccountCreatorError, Unit> =
        accountRepository.find(FindAccountCriteria.ByIdentityNumber(identityNumber))
            ?.let { AccountCreatorError.IdentityNumberAlreadyExists(identityNumber).left() }
            ?: Unit.right()

    private fun guardEmailIsNotRepeated(email: Email): Either<AccountCreatorError, Unit> =
        accountRepository.find(FindAccountCriteria.ByEmail(email))
            ?.let { AccountCreatorError.EmailAlreadyExists(email).left() }
            ?: Unit.right()

    private fun guardPhoneIsNotRepeated(phone: Phone): Either<AccountCreatorError, Unit> =
        accountRepository.find(FindAccountCriteria.ByPhone(phone))
            ?.let { AccountCreatorError.PhoneAlreadyExists(phone).left() }
            ?: Unit.right()

    private fun Account.save(): Unit = accountRepository.save(this).let { }
}

sealed class AccountCreatorError(message: String) : RuntimeException(message) {
    class IdentityNumberAlreadyExists(identityNumber: IdentityNumber) :
        AccountCreatorError("an account with identity number ${identityNumber.value} already exists")

    class EmailAlreadyExists(email: Email) : AccountCreatorError("an account with email ${email.value} already exists")

    class PhoneAlreadyExists(phone: Phone) :
        AccountCreatorError("an account with phone ${phone.prefix.value} ${phone.number.value} already exists")
}

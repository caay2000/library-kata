package com.github.caay2000.librarykata.eventdriven.context.account.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.CreateAccountRequest
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Email
import com.github.caay2000.librarykata.eventdriven.context.account.domain.FindAccountCriteria
import com.github.caay2000.librarykata.eventdriven.context.account.domain.IdentityNumber
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Phone

class AccountCreator(
    private val accountRepository: AccountRepository,
    private val eventPublisher: DomainEventPublisher,
) {
    fun invoke(request: CreateAccountRequest): Either<AccountCreatorError, Unit> =
        guardAccountCanBeCreated(request)
            .map { Account.create(request) }
            .map { account -> accountRepository.save(account) }
            .map { account -> eventPublisher.publish(account.pullEvents()) }

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
}

sealed class AccountCreatorError(message: String) : RuntimeException(message) {
    class IdentityNumberAlreadyExists(identityNumber: IdentityNumber) :
        AccountCreatorError("an account with identity number ${identityNumber.value} already exists")

    class EmailAlreadyExists(email: Email) : AccountCreatorError("an account with email ${email.value} already exists")

    class PhoneAlreadyExists(phone: Phone) :
        AccountCreatorError("an account with phone $phone already exists")
}

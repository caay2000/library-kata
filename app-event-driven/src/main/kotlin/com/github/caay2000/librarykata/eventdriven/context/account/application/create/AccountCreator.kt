package com.github.caay2000.librarykata.eventdriven.context.account.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.CreateAccountRequest
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Email
import com.github.caay2000.librarykata.eventdriven.context.account.domain.FindAccountCriteria
import com.github.caay2000.librarykata.eventdriven.context.account.domain.IdentityNumber
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Phone
import com.github.caay2000.librarykata.eventdriven.context.account.domain.PhoneNumber
import com.github.caay2000.librarykata.eventdriven.context.account.domain.PhonePrefix
import com.github.caay2000.librarykata.eventdriven.context.account.domain.findOrElse

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
            .flatMap { guardPhoneIsNotRepeated(Phone.create(request.phonePrefix.value, request.phoneNumber.value)) }

    private fun guardIdentityNumberIsNotRepeated(identityNumber: IdentityNumber): Either<AccountCreatorError, Unit> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ByIdentityNumber(identityNumber),
            onResourceDoesNotExist = { AccountCreatorError.AccountNotFound() },
        )
            .flatMap { AccountCreatorError.IdentityNumberAlreadyExists(identityNumber).left() }
            .validateAccountNotFound()

    private fun guardEmailIsNotRepeated(email: Email): Either<AccountCreatorError, Unit> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ByEmail(email),
            onResourceDoesNotExist = { AccountCreatorError.AccountNotFound() },
        )
            .flatMap { AccountCreatorError.EmailAlreadyExists(email).left() }
            .validateAccountNotFound()

    private fun guardPhoneIsNotRepeated(phone: Phone): Either<AccountCreatorError, Unit> =
        accountRepository.findOrElse(
            criteria = FindAccountCriteria.ByPhone(phone),
            onResourceDoesNotExist = { AccountCreatorError.AccountNotFound() },
        )
            .flatMap { AccountCreatorError.PhoneAlreadyExists(phone.prefix, phone.number).left() }
            .validateAccountNotFound()

    private fun Either<AccountCreatorError, Unit>.validateAccountNotFound() =
        recover { error ->
            when (error) {
                is AccountCreatorError.AccountNotFound -> Unit.right()
                else -> raise(error)
            }
        }
}

sealed class AccountCreatorError(message: String) : RuntimeException(message) {
    class AccountNotFound : AccountCreatorError("account not foung")

    class IdentityNumberAlreadyExists(identityNumber: IdentityNumber) :
        AccountCreatorError("an account with identity number ${identityNumber.value} already exists")

    class EmailAlreadyExists(email: Email) : AccountCreatorError("an account with email ${email.value} already exists")

    class PhoneAlreadyExists(phonePrefix: PhonePrefix, phoneNumber: PhoneNumber) :
        AccountCreatorError("an account with phone ${phonePrefix.value} ${phoneNumber.value} already exists")
}

package com.github.caay2000.librarykata.eventdriven.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Birthdate
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Email
import com.github.caay2000.librarykata.eventdriven.context.account.domain.IdentityNumber
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Name
import com.github.caay2000.librarykata.eventdriven.context.account.domain.PhoneNumber
import com.github.caay2000.librarykata.eventdriven.context.account.domain.PhonePrefix
import com.github.caay2000.librarykata.eventdriven.context.account.domain.RegisterDate
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Surname
import java.time.LocalDate
import java.time.LocalDateTime

object AccountMother {

    fun random(
        id: AccountId = AccountIdMother.random(),
        identityNumber: IdentityNumber = IdentityNumber(RandomStringGenerator.randomUppercaseLetter() + RandomStringGenerator.randomNumber(8)),
        name: Name = Name(RandomStringGenerator.randomName()),
        surname: Surname = Surname(RandomStringGenerator.randomSurname()),
        birthdate: Birthdate = Birthdate(LocalDate.now()),
        email: Email = Email(RandomStringGenerator.randomEmail()),
        phonePrefix: PhonePrefix = PhonePrefix("+" + RandomStringGenerator.randomNumber(3)),
        phoneNumber: PhoneNumber = PhoneNumber("6" + RandomStringGenerator.randomNumber(8)),
        registerDate: RegisterDate = RegisterDate(LocalDateTime.now()),
    ): Account = Account(
        id = id,
        identityNumber = identityNumber,
        name = name,
        surname = surname,
        birthdate = birthdate,
        email = email,
        phonePrefix = phonePrefix,
        phoneNumber = phoneNumber,
        registerDate = registerDate,
    )
}

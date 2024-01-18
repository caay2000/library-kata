package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Birthdate
import com.github.caay2000.librarykata.hexagonal.context.domain.account.CurrentLoans
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Email
import com.github.caay2000.librarykata.hexagonal.context.domain.account.IdentityNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Name
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhoneNumber
import com.github.caay2000.librarykata.hexagonal.context.domain.account.PhonePrefix
import com.github.caay2000.librarykata.hexagonal.context.domain.account.RegisterDate
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Surname
import com.github.caay2000.librarykata.hexagonal.context.domain.account.TotalLoans
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
        currentLoans: CurrentLoans = CurrentLoans(0),
        totalLoans: TotalLoans = TotalLoans(0),
    ): Account =
        Account(
            id = id,
            identityNumber = identityNumber,
            name = name,
            surname = surname,
            birthdate = birthdate,
            email = email,
            phonePrefix = phonePrefix,
            phoneNumber = phoneNumber,
            registerDate = registerDate,
            currentLoans = currentLoans,
            totalLoans = totalLoans,
        )
}

package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.projectskeleton.context.account.domain.Account
import com.github.caay2000.projectskeleton.context.account.domain.AccountId
import com.github.caay2000.projectskeleton.context.account.domain.Birthdate
import com.github.caay2000.projectskeleton.context.account.domain.Email
import com.github.caay2000.projectskeleton.context.account.domain.IdentityNumber
import com.github.caay2000.projectskeleton.context.account.domain.Name
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix
import com.github.caay2000.projectskeleton.context.account.domain.RegisterDate
import com.github.caay2000.projectskeleton.context.account.domain.Surname

object AccountMother {

    fun random(
        id: AccountId = AccountIdMother.random(),
        identityNumber: IdentityNumber = IdentityNumberMother.random(),
        name: Name = NameMother.random(),
        surname: Surname = SurnameMother.random(),
        birthdate: Birthdate = BirthdateMother.random(),
        email: Email = EmailMother.random(),
        phonePrefix: PhonePrefix = PhonePrefixMother.random(),
        phoneNumber: PhoneNumber = PhoneNumberMother.random(),
        registerDate: RegisterDate = RegisterDateMother.random(),
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

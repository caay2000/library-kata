package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.projectskeleton.context.account.domain.PhoneNumber

object PhoneNumberMother {
    fun random(): PhoneNumber = PhoneNumber("6" + RandomStringGenerator.randomNumber(8))
}

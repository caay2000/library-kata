package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.projectskeleton.context.account.domain.IdentityNumber

object IdentityNumberMother {
    fun random(): IdentityNumber = IdentityNumber(RandomStringGenerator.randomUppercaseLetter() + RandomStringGenerator.randomNumber(8))
}

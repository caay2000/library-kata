package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.projectskeleton.context.account.domain.PhonePrefix

object PhonePrefixMother {
    fun random(): PhonePrefix = PhonePrefix("+" + RandomStringGenerator.randomNumber(3))
}

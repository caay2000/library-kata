package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.projectskeleton.context.account.domain.Email

object EmailMother {
    fun random(): Email = Email(RandomStringGenerator.randomEmail())
}

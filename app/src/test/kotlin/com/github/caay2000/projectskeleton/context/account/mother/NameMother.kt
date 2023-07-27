package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.common.test.RandomStringGenerator
import com.github.caay2000.projectskeleton.context.account.domain.Name

object NameMother {
    fun random(): Name = Name(RandomStringGenerator.randomName())
}

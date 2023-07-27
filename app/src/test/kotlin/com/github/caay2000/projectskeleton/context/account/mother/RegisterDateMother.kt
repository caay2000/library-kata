package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.projectskeleton.context.account.domain.RegisterDate
import java.time.LocalDateTime

object RegisterDateMother {
    fun random(): RegisterDate = RegisterDate(LocalDateTime.now())
}

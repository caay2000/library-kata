package com.github.caay2000.projectskeleton.context.account.mother

import com.github.caay2000.projectskeleton.context.account.domain.Birthdate
import java.time.LocalDate

object BirthdateMother {
    fun random(): Birthdate = Birthdate(LocalDate.now())
}

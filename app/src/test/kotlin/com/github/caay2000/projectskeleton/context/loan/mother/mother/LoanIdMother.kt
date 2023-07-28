package com.github.caay2000.projectskeleton.context.loan.mother.mother

import com.github.caay2000.projectskeleton.context.loan.domain.LoanId
import java.util.UUID

object LoanIdMother {
    fun random(): LoanId = LoanId(UUID.randomUUID())
}

package com.github.caay2000.librarykata.eventdriven.context.loan.mother

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanId
import java.util.UUID

object LoanIdMother {
    fun random(): LoanId = LoanId(UUID.randomUUID())
}

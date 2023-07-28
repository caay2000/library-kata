package com.github.caay2000.librarykata.context.loan.mother

import com.github.caay2000.librarykata.context.loan.domain.LoanId
import java.util.UUID

object LoanIdMother {
    fun random(): LoanId = LoanId(UUID.randomUUID())
}

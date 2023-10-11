package com.github.caay2000.librarykata.hexagonalarrow.context.loan.mother

import com.github.caay2000.librarykata.hexagonalarrow.context.domain.LoanId
import java.util.UUID

object LoanIdMother {
    fun random(): LoanId = LoanId(UUID.randomUUID().toString())
}

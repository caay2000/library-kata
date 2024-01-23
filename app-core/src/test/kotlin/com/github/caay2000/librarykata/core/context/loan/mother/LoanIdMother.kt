package com.github.caay2000.librarykata.core.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanId
import java.util.UUID

object LoanIdMother {
    fun random(): LoanId = LoanId(UUID.randomUUID().toString())
}

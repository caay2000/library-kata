package com.github.caay2000.librarykata.hexagonal.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.LoanId
import java.util.UUID

object LoanIdMother {
    fun random(): LoanId = LoanId(UUID.randomUUID().toString())
}

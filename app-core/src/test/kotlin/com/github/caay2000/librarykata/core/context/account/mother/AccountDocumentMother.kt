package com.github.caay2000.librarykata.core.context.account.mother

import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.transformer.toJsonApiAccountDocument
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan

object AccountDocumentMother {
    internal fun random(account: Account = AccountMother.random()) = account.toJsonApiAccountDocument()

    internal fun random(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
        include: List<String> = emptyList(),
    ) = account.toJsonApiAccountDocument(loans, include)
}

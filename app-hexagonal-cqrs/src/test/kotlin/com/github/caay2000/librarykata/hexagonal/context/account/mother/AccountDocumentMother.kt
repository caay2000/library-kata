package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.toJsonApiDocument

object AccountDocumentMother {
    internal fun random(account: Account = AccountMother.random()) = account.toJsonApiDocument()

    internal fun random(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
        include: List<String> = emptyList(),
    ) = account.toJsonApiDocument(loans, include)
}

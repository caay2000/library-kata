package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.serializer.toJsonApiDocument
import kotlinx.serialization.encodeToString

object AccountDocumentMother {
    internal fun random(account: Account = AccountMother.random()) = account.toJsonApiDocument()

    internal fun random(
        account: Account = AccountMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(accountId = account.id) },
    ) = account.toJsonApiDocument(loans)

//    internal fun json(account: Account = AccountMother.random(), included: Boolean) = jsonMapper.encodeToString(account.toJsonApiDocument(emptyList(),  included))

    internal fun json(
        account: Account = AccountMother.random(),
        loan: Loan,
    ) = jsonMapper.encodeToString(account.toJsonApiDocument(listOf(loan)))

    internal fun json(
        account: Account = AccountMother.random(),
        loans: List<Loan> = emptyList(),
        included: Boolean = false,
    ) = jsonMapper.encodeToString(account.toJsonApiDocument(loans, included))
}

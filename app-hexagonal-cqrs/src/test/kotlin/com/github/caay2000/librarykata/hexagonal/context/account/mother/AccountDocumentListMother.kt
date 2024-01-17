package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer.toJsonApiDocumentList
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import kotlin.random.Random

object AccountDocumentListMother {
    fun random(accounts: List<Account> = listOf(AccountMother.random())): JsonApiDocumentList<AccountResource> = accounts.toJsonApiDocumentList(emptyList())

    fun random(
        accounts: List<Account> = List(Random.nextInt(3)) { AccountMother.random() },
        loans: List<Loan> = emptyList(),
        include: List<String> = emptyList(),
    ): JsonApiDocumentList<AccountResource> = accounts.toJsonApiDocumentList(loans, include)
}

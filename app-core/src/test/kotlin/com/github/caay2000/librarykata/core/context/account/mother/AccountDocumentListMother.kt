package com.github.caay2000.librarykata.core.context.account.mother

import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.account.primaryadapter.http.transformer.toJsonApiAccountDocumentList
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import kotlin.random.Random

object AccountDocumentListMother {
    fun random(accounts: List<Account> = listOf(AccountMother.random())): JsonApiDocumentList<AccountResource> = accounts.toJsonApiAccountDocumentList(emptyList())

    fun random(
        accounts: List<Account> = List(Random.nextInt(3)) { AccountMother.random() },
        loans: List<Loan> = emptyList(),
        include: List<String> = emptyList(),
    ): JsonApiDocumentList<AccountResource> = accounts.toJsonApiAccountDocumentList(loans, include)

    fun empty() = JsonApiDocumentList<AccountResource>(data = emptyList(), meta = JsonApiMeta(0))
}

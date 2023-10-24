package com.github.caay2000.librarykata.hexagonal.context.account.mother

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import kotlinx.serialization.encodeToString
import kotlin.random.Random

object AccountListDocumentMother {

    fun random(account: Account = AccountMother.random()): JsonApiListDocument<AccountResource> =
        JsonApiListDocument(
            data = listOf(account.toAccountResource()),
            meta = JsonApiMeta(total = 1),
        )

    fun random(accounts: List<Account> = List(Random.nextInt(3)) { AccountMother.random() }): JsonApiListDocument<AccountResource> =
        JsonApiListDocument(
            data = accounts.map { it.toAccountResource() },
            meta = JsonApiMeta(total = accounts.size),
        )

    fun json(account: Account = AccountMother.random()) = jsonMapper.encodeToString(random(account))

    fun json(accounts: List<Account> = List(Random.nextInt(3)) { AccountMother.random() }) = jsonMapper.encodeToString(random(accounts))
}

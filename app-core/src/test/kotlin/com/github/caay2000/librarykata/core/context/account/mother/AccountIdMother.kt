package com.github.caay2000.librarykata.core.context.account.mother

import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountId
import java.util.UUID

object AccountIdMother {
    fun random(): AccountId = AccountId(UUID.randomUUID().toString())
}

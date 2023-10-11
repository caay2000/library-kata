package com.github.caay2000.librarykata.hexagonalarrow.context.account.mother

import com.github.caay2000.librarykata.hexagonalarrow.context.domain.AccountId
import java.util.UUID

object AccountIdMother {
    fun random(): AccountId = AccountId(UUID.randomUUID().toString())
}

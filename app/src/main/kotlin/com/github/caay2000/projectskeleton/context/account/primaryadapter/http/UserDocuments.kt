package com.github.caay2000.projectskeleton.context.account.primaryadapter.http

import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.projectskeleton.context.account.domain.Account
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AccountDetailsDocument(

    @Serializable(with = UUIDSerializer::class)
    val accountId: UUID,
    val email: String,
    val name: String,
)

fun Account.toAccountDetailsDocument() = AccountDetailsDocument(
    accountId = this.id.value,
    email = this.email.value,
    name = this.name.value,
)

@Serializable
data class CreateAccountRequestDocument(
    val email: String,
    val name: String,
)

package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LoanByAccountIdDocument(
    @Serializable(with = UUIDSerializer::class)
    val accountId: UUID,
    val loans: List<LoanDocument>,
)

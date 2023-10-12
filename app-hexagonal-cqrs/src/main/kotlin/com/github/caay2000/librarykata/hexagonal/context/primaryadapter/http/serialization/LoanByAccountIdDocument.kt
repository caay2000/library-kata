package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import java.util.UUID

data class LoanByAccountIdDocument(
    val accountId: UUID,
    val loans: List<LoanDocument>,
)

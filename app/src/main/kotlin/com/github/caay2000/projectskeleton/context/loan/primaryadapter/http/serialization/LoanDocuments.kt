package com.github.caay2000.projectskeleton.context.loan.primaryadapter.http.serialization

import com.github.caay2000.common.serialization.UUIDSerializer
import com.github.caay2000.projectskeleton.context.loan.domain.Loan
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class LoanDocument(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val bookId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
)

fun Loan.toLoanDocument() = LoanDocument(
    id = this.id.value,
    bookId = this.bookId.value,
    userId = this.userId.value,
)

@Serializable
data class LoanRequestDocument(
    val bookIsbn: String,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
)

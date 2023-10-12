package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import java.time.LocalDateTime

data class LoanDocument(
    override val data: Resource,

    override val included: List<JsonApiRelationshipResource> = emptyList(),
) : JsonApiDocument {

    data class Resource(
        override val id: String,
        override val type: String = "loan",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipIdentifier> = emptyList(),
    ) : JsonApiResource {

        data class Attributes(
            val bookId: String,
            val accountId: String,
            val startLoan: LocalDateTime,
            val finishLoan: LocalDateTime?,
        ) : JsonApiResourceAttributes
    }
}

fun Loan.toLoanDocument() =
    LoanDocument(
        data = LoanDocument.Resource(
            id = id.value,
            attributes = toLoanDocumentAttributes(),
        ),
    )

fun Loan.toLoanDocumentAttributes() =
    LoanDocument.Resource.Attributes(
        bookId = bookId.value,
        accountId = accountId.value,
        startLoan = createdAt.value,
        finishLoan = finishedAt?.value,
    )

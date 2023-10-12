package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.common.jsonapi.JsonApiRelationshipResource
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import com.github.caay2000.common.serialization.LocalDateTimeSerializer
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LoanDocument(
    override val data: Resource,
    @Serializable
    override val included: List<JsonApiRelationshipResource> = emptyList(),
) : JsonApiDocument {

    @Serializable
    data class Resource(
        override val id: String,
        override val type: String = "loan",
        override val attributes: Attributes,
        override val relationships: List<JsonApiRelationshipIdentifier> = emptyList(),
    ) : JsonApiResource {

        @Serializable
        @SerialName("loan")
        data class Attributes(
            val bookId: String,
            val accountId: String,
            @Serializable(with = LocalDateTimeSerializer::class)
            val startLoan: LocalDateTime,
            @Serializable(with = LocalDateTimeSerializer::class)
            val finishLoan: LocalDateTime?,
        ) : JsonApiResourceAttributes
    }

//    @Serializable
//    data class LoanRelationshipIdentifier(
//        override val id: String,
//        override val type: String = "loan",
//    ) : JsonApiRelationshipIdentifier
//
//    @Serializable
//    data class LoanRelationshipResource(
//        override val id: String,
//        override val type: String = "loan",
//        override val attributes: Resource.Attributes,
//    ) : JsonApiRelationshipResource
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

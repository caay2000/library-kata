package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan

fun Loan.toJsonApiDocument(): JsonApiDocument<LoanResource> =
    JsonApiDocument(
        data = LoanResource(
            id = id.value,
            attributes = toJsonApiDocumentAttributes(),
        ),
    )

fun Loan.toJsonApiDocumentAttributes() =
    LoanResource.Attributes(
        bookId = bookId.value,
        accountId = accountId.value,
        startLoan = createdAt.value,
        finishLoan = finishedAt?.value,
    )

fun List<Loan>.toJsonApiDocumentIncludedResource(): List<JsonApiIncludedResource>? =
    if (isEmpty()) {
        null
    } else {
        this.map {
            JsonApiIncludedResource(
                id = it.id.value,
                type = "loan",
                attributes = it.toJsonApiDocumentAttributes(),
                relationships = null,
            )
        }
    }

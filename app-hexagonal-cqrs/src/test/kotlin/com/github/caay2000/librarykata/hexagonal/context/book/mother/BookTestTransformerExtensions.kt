package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentAttributes
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

internal fun Book.toJsonApiBookDocument(
    loans: List<Loan> = emptyList(),
    included: List<String> = emptyList(),
) = JsonApiDocument(
    data = toBookResource(loans),
    included = mapIncluded(included, loans),
)

internal fun Book.toBookResource(loans: List<Loan> = emptyList()) =
    BookResource(
        id = id.value,
        type = "book",
        attributes =
            BookResource.Attributes(
                isbn = isbn.value,
                title = title.value,
                author = author.value,
                pages = pages.value,
                publisher = publisher.value,
                available = available.value,
            ),
        relationships = mapRelationships(loans),
    )

private fun mapIncluded(
    included: List<String>,
    loans: List<Loan>,
): List<JsonApiIncludedResource>? =
    if (included.contains("LOANS")) {
        if (loans.isEmpty()) {
            null
        } else {
            loans.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = "loan",
                    attributes = it.toJsonApiDocumentAttributes(),
                )
            }
        }
    } else {
        null
    }

private fun mapRelationships(loans: List<Loan>): Map<String, JsonApiRelationshipData>? =
    if (loans.isEmpty()) {
        null
    } else {
        mapOf(
            "loan" to
                JsonApiRelationshipData(
                    loans.map { JsonApiRelationshipIdentifier(id = it.id.value, type = "loan") },
                ),
        )
    }

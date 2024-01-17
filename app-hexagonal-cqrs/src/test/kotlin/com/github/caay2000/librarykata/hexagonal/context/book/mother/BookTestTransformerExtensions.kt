package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentAttributes
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

internal fun Book.toJsonApiBookDocument(
    loans: List<Loan> = emptyList(),
    include: List<String> = emptyList(),
) = JsonApiDocument(
    data = toBookResource(loans),
    included = mapIncluded(include, loans),
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
    if (included.contains(LoanResource.type)) {
        if (loans.isEmpty()) {
            null
        } else {
            loans.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = LoanResource.type,
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
            LoanResource.type to
                JsonApiRelationshipData(
                    loans.map { JsonApiRelationshipIdentifier(id = it.id.value, type = "loan") },
                ),
        )
    }

package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentAttributes
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

internal fun List<Book>.toJsonApiBookGroupDocument(loans: List<Loan> = emptyList()) =
    JsonApiListDocument(
        data = toBookGroupResource(loans),
        meta = JsonApiMeta(total = size),
    )

internal fun List<Book>.toBookGroupResource(loans: List<Loan> = emptyList()): List<BookGroupResource> =
    this.groupBy { it.isbn }.map { it.value }
        .map {
            BookGroupResource(
                id = first().isbn.value,
                type = "bookGroup",
                attributes =
                    BookGroupResource.Attributes(
                        isbn = first().isbn.value,
                        title = first().title.value,
                        author = first().author.value,
                        pages = first().pages.value,
                        publisher = first().publisher.value,
                        copies = size,
                        availableCopies = count { it.isAvailable },
                    ),
                relationships = mapRelationships(loans),
            )
        }

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

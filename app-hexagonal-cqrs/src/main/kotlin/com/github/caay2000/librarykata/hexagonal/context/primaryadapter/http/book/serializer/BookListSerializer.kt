package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.serializer

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

internal fun List<Book>.toJsonApiDocumentBookGroupResource(loans: List<Loan> = emptyList()) =
    BookGroupResource(
        id = first().isbn.value,
        type = "book-group",
        attributes = toJsonApiDocumentBookGroupAttributes(),
        relationships = mapRelationships(loans),
    )

private fun List<Book>.toJsonApiDocumentBookGroupAttributes() =
    BookGroupResource.Attributes(
        isbn = first().isbn.value,
        title = first().title.value,
        author = first().author.value,
        pages = first().pages.value,
        publisher = first().publisher.value,
        copies = size,
        availableCopies = count { it.isAvailable },
    )

// private fun List<Book>.toJsonApiDocumentIncludedResource() =
//    map {
//        BookResource(
//            id = it.id.value,
//            type = "book",
//            attributes = it.toJsonApiDocumentBookAttributes(),
//        )
//    }

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

package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.serializer

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

internal fun Book.toJsonApiDocumentBookResource(loans: List<Loan> = emptyList()) =
    BookResource(
        id = id.value,
        type = "book",
        attributes = toJsonApiDocumentBookAttributes(),
        relationships = mapRelationships(loans),
    )

private fun Book.toJsonApiDocumentBookAttributes() =
    BookResource.Attributes(
        isbn = isbn.value,
        title = title.value,
        author = author.value,
        pages = pages.value,
        publisher = publisher.value,
        available = available.value,
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

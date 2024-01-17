package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

internal fun List<Book>.toJsonApiBookGroupDocument(loans: List<Loan> = emptyList()) =
    JsonApiDocumentList(
        data = toBookGroupResource(loans),
        included = null,
        meta = JsonApiMeta(total = groupBy { it.isbn }.size),
    )

internal fun List<Book>.toBookGroupResource(loans: List<Loan> = emptyList()): List<BookGroupResource> =
    this.groupBy { it.isbn }.map { it.value }
        .map { list ->
            BookGroupResource(
                id = list.first().isbn.value,
                type = "book-group",
                attributes =
                    BookGroupResource.Attributes(
                        isbn = list.first().isbn.value,
                        title = list.first().title.value,
                        author = list.first().author.value,
                        pages = list.first().pages.value,
                        publisher = list.first().publisher.value,
                        copies = list.size,
                        availableCopies = list.count { it.isAvailable },
                    ),
                relationships = mapRelationships(loans.filter { list.map { it.id }.contains(it.bookId) }),
            )
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

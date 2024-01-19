package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookRelationshipTransformer : Transformer<Collection<Book>, Map<String, JsonApiRelationshipData>?> {
    override fun invoke(
        value: Collection<Book>,
        include: List<String>,
    ): Map<String, JsonApiRelationshipData>? =
        if (value.isEmpty()) {
            null
        } else {
            mapOf(
                BookResource.TYPE to
                    JsonApiRelationshipData(
                        value.map { JsonApiRelationshipIdentifier(id = it.id.value, type = BookResource.TYPE) },
                    ),
            )
        }
}

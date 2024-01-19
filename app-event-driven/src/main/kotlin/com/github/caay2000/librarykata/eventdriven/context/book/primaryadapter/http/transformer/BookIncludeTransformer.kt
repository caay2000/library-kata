package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource

class BookIncludeTransformer : Transformer<Collection<Book>, List<JsonApiIncludedResource>?> {
    override fun invoke(
        value: Collection<Book>,
        include: List<String>,
    ): List<JsonApiIncludedResource>? =
        if (value.isEmpty()) {
            null
        } else {
            value.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = BookResource.TYPE,
                    attributes = it.toJsonApiBookAttributes(),
                )
            }
        }
}

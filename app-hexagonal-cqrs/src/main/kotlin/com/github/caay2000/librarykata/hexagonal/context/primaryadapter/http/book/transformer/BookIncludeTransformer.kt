package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
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

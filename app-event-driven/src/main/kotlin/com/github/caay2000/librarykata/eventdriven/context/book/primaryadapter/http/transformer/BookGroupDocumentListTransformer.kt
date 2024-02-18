package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

class BookGroupDocumentListTransformer : Transformer<List<Book>, JsonApiDocumentList<BookGroupResource>> {
    private val resourceTransformer = BookGroupResourceTransformer()

    override fun invoke(
        value: List<Book>,
        include: List<String>,
    ): JsonApiDocumentList<BookGroupResource> {
        val groupBy = value.groupBy { it.isbn }
        return JsonApiDocumentList(
            data = groupBy.map { resourceTransformer.invoke(it.value) },
            included = null,
            meta = JsonApiMeta(total = groupBy.size),
        )
    }
}

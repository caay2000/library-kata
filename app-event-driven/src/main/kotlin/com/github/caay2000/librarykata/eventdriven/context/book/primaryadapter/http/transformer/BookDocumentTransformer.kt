package com.github.caay2000.librarykata.eventdriven.context.book.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.toJsonApiIncludedResource
import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryBus
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class BookDocumentTransformer(private val queryBus: ResourceQueryBus) : Transformer<Book, JsonApiDocument<BookResource>> {
    private val resourceTransformer = BookResourceTransformer()

    override fun invoke(
        value: Book,
        include: List<String>,
    ): JsonApiDocument<BookResource> {
        val resource = resourceTransformer.invoke(value)
        return JsonApiDocument(
            data = resource,
            included = handleIncludes(resource, include),
        )
    }

    private fun handleIncludes(
        resource: BookResource,
        include: List<String>,
    ): Collection<JsonApiIncludedResource>? {
        val includes = mutableListOf<JsonApiIncludedResource>()
        if (include.contains("loan")) {
            val loans =
                resource.findAllRelationshipWithType(LoanResource.TYPE).map {
                    queryBus.invoke(ResourceQuery(it.id, LoanResource.TYPE)).resource.toJsonApiIncludedResource()
                }
            includes.addAll(loans)
        }
        return includes.ifEmpty { null }
    }
}

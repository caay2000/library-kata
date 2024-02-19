package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.toJsonApiIncludedResource
import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryBus
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentTransformer(val queryBus: ResourceQueryBus) : Transformer<Loan, JsonApiDocument<LoanResource>> {
    private val resourceTransformer = LoanResourceTransformer()

    override fun invoke(
        value: Loan,
        include: List<String>,
    ): JsonApiDocument<LoanResource> {
        val resource = resourceTransformer.invoke(value)
        return JsonApiDocument(
            data = resource,
            included = handleIncludes(resource, include.map { it.lowercase() }),
        )
    }

    private fun handleIncludes(
        resource: LoanResource,
        include: List<String>,
    ): Collection<JsonApiIncludedResource>? {
        val includes = mutableListOf<JsonApiIncludedResource>()
        if (include.contains("account")) {
            val loans =
                resource.findAllRelationshipWithType(AccountResource.TYPE).map {
                    queryBus.invoke(ResourceQuery(it.id, AccountResource.TYPE)).resource.toJsonApiIncludedResource()
                }
            includes.addAll(loans)
        }

        if (include.contains("book")) {
            val loans =
                resource.findAllRelationshipWithType(BookResource.TYPE).map {
                    queryBus.invoke(ResourceQuery(it.id, BookResource.TYPE)).resource.toJsonApiIncludedResource()
                }
            includes.addAll(loans)
        }
        return includes.ifEmpty { null }
    }
}

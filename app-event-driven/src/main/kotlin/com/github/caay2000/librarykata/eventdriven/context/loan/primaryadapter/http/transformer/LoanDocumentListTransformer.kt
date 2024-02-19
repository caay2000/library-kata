package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.toJsonApiIncludedResource
import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryBus
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentListTransformer(private val queryBus: ResourceQueryBus) : Transformer<List<Loan>, JsonApiDocumentList<LoanResource>> {
    private val resourceTransformer = LoanResourceTransformer()

    override fun invoke(
        value: List<Loan>,
        include: List<String>,
    ): JsonApiDocumentList<LoanResource> = value.toJsonApiLoanDocumentList(include)

    private fun List<Loan>.toJsonApiLoanDocumentList(include: List<String> = emptyList()): JsonApiDocumentList<LoanResource> {
        val resources = map { resourceTransformer.invoke(it) }
        return JsonApiDocumentList(
            data = resources,
            included = handleIncludes(resources, include.map { it.lowercase() }),
            meta = JsonApiMeta(total = size),
        )
    }

    private fun handleIncludes(
        resources: List<LoanResource>,
        include: List<String>,
    ): Collection<JsonApiIncludedResource>? {
        val includes = mutableListOf<JsonApiIncludedResource>()
        if (include.contains("account")) {
            val loans =
                resources.flatMap {
                    it.findAllRelationshipWithType(AccountResource.TYPE).map {
                        queryBus.invoke(ResourceQuery(it.id, AccountResource.TYPE)).resource.toJsonApiIncludedResource()
                    }
                }
            includes.addAll(loans)
        }

        if (include.contains("book")) {
            val loans =
                resources.flatMap {
                    it.findAllRelationshipWithType(BookResource.TYPE).map {
                        queryBus.invoke(ResourceQuery(it.id, BookResource.TYPE)).resource.toJsonApiIncludedResource()
                    }
                }
            includes.addAll(loans)
        }
        return includes.ifEmpty { null }
    }
}

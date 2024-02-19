package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.common.jsonapi.toJsonApiIncludedResource
import com.github.caay2000.common.query.ResourceQuery
import com.github.caay2000.common.query.ResourceQueryBus
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class AccountDocumentListTransformer(loanRepository: LoanRepository, private val queryBus: ResourceQueryBus) : Transformer<List<Account>, JsonApiDocumentList<AccountResource>> {
    private val resourceTransformer = AccountResourceTransformer(loanRepository)

    override fun invoke(
        value: List<Account>,
        include: List<String>,
    ): JsonApiDocumentList<AccountResource> = value.toJsonApiAccountDocumentList(include)

    private fun List<Account>.toJsonApiAccountDocumentList(include: List<String> = emptyList()): JsonApiDocumentList<AccountResource> {
        val resources = map { resourceTransformer.invoke(it) }
        return JsonApiDocumentList(
            data = resources,
            included = handleIncludes(resources, include.map { it.lowercase() }),
            meta = JsonApiMeta(total = size),
        )
    }

    private fun handleIncludes(
        resources: List<AccountResource>,
        include: List<String>,
    ): Collection<JsonApiIncludedResource>? {
        val includes = mutableListOf<JsonApiIncludedResource>()
        if (include.contains("loan")) {
            val loans =
                resources.flatMap {
                    it.findAllRelationshipWithType(LoanResource.TYPE).map {
                        queryBus.invoke(ResourceQuery(it.id, LoanResource.TYPE)).resource.toJsonApiIncludedResource()
                    }
                }
            includes.addAll(loans)
        }
        return includes.ifEmpty { null }
    }
}

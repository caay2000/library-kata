package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.toJsonApiIncludedResource
import com.github.caay2000.common.querybus.SyncQueryBusHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Account
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.LoanResourceQuery
import com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer.LoanResourceQueryResponse
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class AccountDocumentTransformer(loanRepository: LoanRepository, private val queryBusHandler: SyncQueryBusHandler) : Transformer<Account, JsonApiDocument<AccountResource>> {
    private val resourceTransformer = AccountResourceTransformer(loanRepository)

    override fun invoke(
        value: Account,
        include: List<String>,
    ): JsonApiDocument<AccountResource> {
        val resource = resourceTransformer.invoke(value)
        return JsonApiDocument(
            data = resource,
            included = handleIncludes(resource, include.map { it.lowercase() }),
        )
    }

    private fun handleIncludes(
        resource: AccountResource,
        include: List<String>,
    ): Collection<JsonApiIncludedResource>? {
        val includes = mutableListOf<JsonApiIncludedResource>()
        if (include.contains("loan")) {
            val loans =
                resource.findAllRelationshipWithType(LoanResource.TYPE).map {
                    queryBusHandler.invoke<LoanResourceQuery, LoanResourceQueryResponse>(LoanResourceQuery.ByIdentifier(it.id)).resource.toJsonApiIncludedResource()
                }
            includes.addAll(loans)
        }
        return includes.ifEmpty { null }
    }
}

package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.http.resourcebus

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiRequestParams
import com.github.caay2000.common.jsonapi.toJsonApiIncludedResource
import com.github.caay2000.common.resourcebus.JsonApiResourceBus
import com.github.caay2000.librarykata.eventdriven.context.account.application.search.SearchAccountQuery
import com.github.caay2000.librarykata.jsonapi.JsonApiBuilder
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class JsonApiAccountBuilder(private val resourceBus: JsonApiResourceBus) : JsonApiBuilder<AccountResource> {
    override fun getResource(
        identifier: String,
        params: JsonApiRequestParams,
    ): AccountResource = resourceBus.retrieve<AccountResource>(identifier)

    override fun getDocument(
        identifier: String,
        params: JsonApiRequestParams,
    ): JsonApiDocument<AccountResource> {
        val account = getResource(identifier)
        val included: List<JsonApiIncludedResource> =
            params.include.flatMap { include ->
                when (include) {
                    LoanResource.TYPE.uppercase() -> handleLoanIncludes(account)
                    else -> emptyList()
                }
            }
        return JsonApiDocument(
            data = account,
            included = included.ifEmpty { null },
        )
    }

    override fun getDocumentList(params: JsonApiRequestParams): JsonApiDocumentList<AccountResource> {






            when {
                filter.containsKey("phoneNumber") -> SearchAccountQuery.SearchAccountByPhoneNumberQuery(filter["phoneNumber"]!!.first())
                filter.containsKey("email") -> SearchAccountQuery.SearchAccountByEmailQuery(filter["email"]!!.first())
                else -> SearchAccountQuery.SearchAllAccountQuery
            }

    }

    private fun handleLoanIncludes(account: AccountResource): List<JsonApiIncludedResource> {
        val loans =
            account.relationships?.get(LoanResource.TYPE)?.data?.map {
                it.id
            }
        return loans?.map { resourceBus.retrieve<LoanResource>(it).toJsonApiIncludedResource() } ?: emptyList()
    }
}

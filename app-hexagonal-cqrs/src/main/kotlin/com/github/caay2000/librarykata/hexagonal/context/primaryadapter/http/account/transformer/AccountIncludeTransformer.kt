package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

class AccountIncludeTransformer : Transformer<Collection<Account>, List<JsonApiIncludedResource>?> {
    override fun invoke(
        value: Collection<Account>,
        include: List<String>,
    ): List<JsonApiIncludedResource>? =
        if (value.isEmpty()) {
            null
        } else {
            value.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = AccountResource.TYPE,
                    attributes = it.toJsonApiAccountAttributes(),
                )
            }
        }
}

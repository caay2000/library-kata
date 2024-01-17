package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

class AccountRelationshipTransformer : Transformer<Collection<Account>, Map<String, JsonApiRelationshipData>?> {
    override fun invoke(
        value: Collection<Account>,
        include: List<String>,
    ): Map<String, JsonApiRelationshipData>? =
        if (value.isEmpty()) {
            null
        } else {
            mapOf(
                AccountResource.TYPE to
                    JsonApiRelationshipData(
                        value.map { JsonApiRelationshipIdentifier(id = it.id.value, type = AccountResource.TYPE) },
                    ),
            )
        }
}

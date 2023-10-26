package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource

class AccountListToAccountDocumentListTransformer : Transformer<List<Account>, JsonApiListDocument<AccountResource>> {

    override fun invoke(value: List<Account>, includes: List<String>): JsonApiListDocument<AccountResource> {
        return JsonApiListDocument(
            data = value.map { it.toJsonApiDocumentResource() },
            meta = JsonApiMeta(total = value.size),
        )
    }
}

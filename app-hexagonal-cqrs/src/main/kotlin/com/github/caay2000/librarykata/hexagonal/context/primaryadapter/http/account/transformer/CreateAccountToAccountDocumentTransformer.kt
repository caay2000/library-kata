package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toJsonApiDocumentResource

class CreateAccountToAccountDocumentTransformer : Transformer<Account, JsonApiDocument<AccountResource>> {

    override fun invoke(value: Account, includes: List<String>): JsonApiDocument<AccountResource> =
        JsonApiDocument(data = value.toJsonApiDocumentResource())
}

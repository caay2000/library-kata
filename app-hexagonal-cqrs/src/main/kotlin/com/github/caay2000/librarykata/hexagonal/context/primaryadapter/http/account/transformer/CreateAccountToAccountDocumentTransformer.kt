package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.account.transformer

import com.github.caay2000.common.http.Transfomer
import com.github.caay2000.librarykata.hexagonal.context.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.AccountDocument
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.toAccountDocumentResource

class CreateAccountToAccountDocumentTransformer : Transfomer<Account, AccountDocument> {

    override fun invoke(value: Account, includes: List<String>): AccountDocument =
        AccountDocument(data = value.toAccountDocumentResource())
}

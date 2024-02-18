package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentTransformer : Transformer<Loan, JsonApiDocument<LoanResource>> {
    private val resourceTransformer = LoanResourceTransformer()

    override fun invoke(
        value: Loan,
        include: List<String>,
    ): JsonApiDocument<LoanResource> =
        JsonApiDocument(
            data = resourceTransformer.invoke(value),
            included = null,
        )
}

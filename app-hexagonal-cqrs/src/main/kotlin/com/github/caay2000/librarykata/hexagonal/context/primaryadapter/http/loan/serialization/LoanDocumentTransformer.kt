package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanDocumentTransformer : Transformer<Loan, JsonApiDocument<LoanResource>> {
    override fun invoke(
        value: Loan,
        include: List<String>,
    ): JsonApiDocument<LoanResource> {
        return value.toJsonApiDocument()
    }
}

internal fun Loan.toJsonApiDocument(): JsonApiDocument<LoanResource> =
    JsonApiDocument(
        data =
            LoanResource(
                id = id.value,
                attributes = toJsonApiDocumentAttributes(),
            ),
    )

internal fun Loan.toJsonApiDocumentAttributes() =
    LoanResource.Attributes(
        bookId = bookId.value,
        accountId = accountId.value,
        startLoan = createdAt.value,
        finishLoan = finishedAt?.value,
    )

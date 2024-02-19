package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.http.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.jsonapi.context.account.AccountResource
import com.github.caay2000.librarykata.jsonapi.context.book.BookResource
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.transformer.RelationshipTransformer

class LoanResourceTransformer : Transformer<Loan, LoanResource> {
    override fun invoke(
        value: Loan,
        include: List<String>,
    ): LoanResource =
        LoanResource(
            id = value.id.value,
            type = LoanResource.TYPE,
            attributes = value.toJsonApiDocumentLoanAttributes(),
            relationships = manageLoanRelationships(value),
        )

    private fun Loan.toJsonApiDocumentLoanAttributes() =
        LoanResource.Attributes(
            bookId = bookId.value,
            accountId = accountId.value,
            startLoan = createdAt.value,
            finishLoan = finishedAt?.value,
        )

    private fun manageLoanRelationships(loan: Loan): Map<String, JsonApiRelationshipData> {
        val map = mutableMapOf<String, JsonApiRelationshipData>()
        map.putAll(RelationshipTransformer.invoke(RelationshipIdentifier(loan.accountId.value, AccountResource.TYPE))!!)
        map.putAll(RelationshipTransformer.invoke(RelationshipIdentifier(loan.bookId.value, BookResource.TYPE))!!)
        return map
    }
}

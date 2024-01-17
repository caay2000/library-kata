package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanRelationshipTransformer : Transformer<Collection<Loan>, Map<String, JsonApiRelationshipData>?> {
    override fun invoke(
        value: Collection<Loan>,
        include: List<String>,
    ): Map<String, JsonApiRelationshipData>? =
        if (value.isEmpty()) {
            null
        } else {
            mapOf(
                LoanResource.TYPE to
                    JsonApiRelationshipData(
                        value.map { JsonApiRelationshipIdentifier(id = it.id.value, type = LoanResource.TYPE) },
                    ),
            )
        }
}

package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class LoanIncludeTransformer : Transformer<Collection<Loan>, List<JsonApiIncludedResource>?> {
    override fun invoke(
        value: Collection<Loan>,
        include: List<String>,
    ): List<JsonApiIncludedResource>? =
        if (value.isEmpty()) {
            null
        } else {
            value.map {
                JsonApiIncludedResource(
                    id = it.id.value,
                    type = LoanResource.TYPE,
                    attributes = it.toJsonApiDocumentAttributes(),
                )
            }
        }
}

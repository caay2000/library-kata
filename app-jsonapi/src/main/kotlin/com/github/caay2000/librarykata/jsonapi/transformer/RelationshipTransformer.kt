package com.github.caay2000.librarykata.jsonapi.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

class RelationshipTransformer : Transformer<Collection<RelationshipIdentifier>, Map<String, JsonApiRelationshipData>?> {
    override fun invoke(
        value: Collection<RelationshipIdentifier>,
        include: List<String>,
    ): Map<String, JsonApiRelationshipData>? =
        if (value.isEmpty()) {
            null
        } else {
            mapOf(
                LoanResource.TYPE to
                    JsonApiRelationshipData(
                        value.map { JsonApiRelationshipIdentifier(id = it.id, type = it.type) },
                    ),
            )
        }
}

data class RelationshipIdentifier(
    val id: String,
    val type: String,
)

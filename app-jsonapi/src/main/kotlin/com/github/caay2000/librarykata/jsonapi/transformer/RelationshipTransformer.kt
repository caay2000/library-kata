package com.github.caay2000.librarykata.jsonapi.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiRelationshipIdentifier
import com.github.caay2000.librarykata.jsonapi.context.loan.LoanResource

object RelationshipTransformer : Transformer<Collection<RelationshipIdentifier>, Map<String, JsonApiRelationshipData>?> {
    fun invoke(
        value: RelationshipIdentifier,
        include: List<String> = emptyList(),
    ): Map<String, JsonApiRelationshipData>? = this.invoke(listOf(value), include)

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

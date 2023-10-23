package com.github.caay2000.common.jsonapi.context.book

import com.github.caay2000.common.jsonapi.JsonApiRelationshipData
import com.github.caay2000.common.jsonapi.JsonApiResource
import com.github.caay2000.common.jsonapi.JsonApiResourceAttributes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("bookByIdResource")
data class BookByIdResource(
    override val id: String,
    override val type: String = "book",
    override val attributes: Attributes,
    override val relationships: Map<String, JsonApiRelationshipData>? = null,
) : JsonApiResource {

    @Serializable
    @SerialName("bookById")
    data class Attributes(
        val isbn: String,
        val title: String,
        val author: String,
        val pages: Int,
        val publisher: String,
        val available: Boolean,
    ) : JsonApiResourceAttributes
}

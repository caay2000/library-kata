package com.github.caay2000.common.jsonapi.context.book

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import kotlinx.serialization.Serializable

@Serializable
data class BookRequestResource(
    override val type: String = "book",
    override val attributes: Attributes,
) : JsonApiRequestResource {

    @Serializable
    data class Attributes(
        val isbn: String,
        val title: String,
        val author: String,
        val pages: Int,
        val publisher: String,
    ) : JsonApiRequestAttributes
}

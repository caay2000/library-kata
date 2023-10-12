package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestResource

data class BookRequestDocument(
    override val data: Resource,
) : JsonApiRequestDocument {

    data class Resource(
        override val type: String = "book",
        override val attributes: Attributes,
    ) : JsonApiRequestResource {

        data class Attributes(
            val isbn: String,
            val title: String,
            val author: String,
            val pages: Int,
            val publisher: String,
        ) : JsonApiRequestAttributes
    }
}

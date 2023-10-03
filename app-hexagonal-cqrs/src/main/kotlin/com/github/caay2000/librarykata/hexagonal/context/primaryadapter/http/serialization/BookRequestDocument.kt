package com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization

import com.github.caay2000.common.jsonapi.JsonApiRequestAttributes
import com.github.caay2000.common.jsonapi.JsonApiRequestDocument
import com.github.caay2000.common.jsonapi.JsonApiRequestResource
import kotlinx.serialization.Serializable

@Serializable
data class BookRequestDocument(
    override val data: BookRequestResource,
) : JsonApiRequestDocument

@Serializable
data class BookRequestResource(
    override val type: String = "book",
    override val attributes: BookRequestAttributes,
) : JsonApiRequestResource

@Serializable
data class BookRequestAttributes(
    val isbn: String,
    val title: String,
    val author: String,
    val pages: Int,
    val publisher: String,
) : JsonApiRequestAttributes

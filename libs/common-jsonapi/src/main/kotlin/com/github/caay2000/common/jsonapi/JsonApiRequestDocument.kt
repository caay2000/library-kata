package com.github.caay2000.common.jsonapi

import kotlinx.serialization.Serializable

@Serializable
data class JsonApiRequestDocument<R : JsonApiRequestResource>(
    val data: R,
)

interface JsonApiRequestResource {
    val type: String

    @Serializable
    val attributes: JsonApiRequestAttributes
}

interface JsonApiRequestAttributes

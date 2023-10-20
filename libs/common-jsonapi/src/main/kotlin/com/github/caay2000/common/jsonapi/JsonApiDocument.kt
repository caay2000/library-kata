package com.github.caay2000.common.jsonapi

import kotlinx.serialization.Serializable

@Serializable
data class JsonApiDocument<R : JsonApiResource>(
    val data: R,
    val included: List<JsonApiIncludedResource>? = null,
)

@Serializable
data class JsonApiListDocument<R : JsonApiResource>(
    val data: List<R>,
    val meta: JsonApiMeta,
)

interface JsonApiResource {
    val id: String
    val type: String
    val attributes: JsonApiResourceAttributes
    val relationships: Map<String, JsonApiRelationshipData>?
}

interface JsonApiResourceAttributes

@Serializable
data class JsonApiRelationshipData(val data: List<JsonApiRelationshipIdentifier>)

@Serializable
data class JsonApiRelationshipIdentifier(
    val id: String,
    val type: String,
)

@Serializable
data class JsonApiIncludedResource(
    val id: String,
    val type: String,
    val attributes: JsonApiResourceAttributes,
)

@Serializable
data class JsonApiMeta(val total: Int)

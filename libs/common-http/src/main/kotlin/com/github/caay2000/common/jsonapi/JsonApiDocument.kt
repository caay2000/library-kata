package com.github.caay2000.common.jsonapi

import kotlinx.serialization.Serializable

@Serializable
data class JsonApiDocument<R>(
    val data: R,
    val included: List<JsonApiIncludedResource>? = null,
) where R : JsonApiResource

@Serializable
data class JsonApiListDocument<R>(
    val data: List<R>,
    val meta: JsonApiMeta,
) where R : JsonApiResource

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

// TODO this should be deleted and JsonApiDocument should use the JsonApiResource interface
// Until kotlin serializer does not allow to hide classDiscriminator, this won't pass jsonapi schema validator
@Serializable
data class JsonApiIncludedResource(
    val id: String,
    val type: String,
    val attributes: JsonApiResourceAttributes,
    val relationships: Map<String, JsonApiRelationshipData>?,
)

@Serializable
data class JsonApiMeta(val total: Int)

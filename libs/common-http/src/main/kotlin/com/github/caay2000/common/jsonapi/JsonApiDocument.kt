package com.github.caay2000.common.jsonapi

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
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
data class JsonApiRelationshipData(
    @field:ArraySchema(contains = Schema(implementation = JsonApiRelationshipIdentifier::class))
    val data: List<JsonApiRelationshipIdentifier>,
)

@Serializable
data class JsonApiRelationshipIdentifier(
    @field:Schema(description = "resource id", example = "00000000-0000-0000-0000-000000000000")
    val id: String,
    @field:Schema(description = "resource type", example = "type")
    val type: String,
)

// TODO this should be deleted and JsonApiDocument should use the JsonApiResource interface
// Until kotlin serializer does not allow to hide classDiscriminator, this won't pass jsonapi schema validator
@Serializable
data class JsonApiIncludedResource(
    @field:Schema(description = "resource id", example = "00000000-0000-0000-0000-000000000000")
    val id: String,
    @field:Schema(description = "resource type", example = "type")
    val type: String,
    val attributes: JsonApiResourceAttributes,
    // TODO should be enabled when an include has a relationship
    // val relationships: Map<String, JsonApiRelationshipData>?,
)

@Serializable
data class JsonApiMeta(val total: Int)

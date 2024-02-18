package com.github.caay2000.common.jsonapi

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import kotlinx.serialization.Serializable

@Serializable
@Schema(name = "JsonApiDocument")
data class JsonApiDocument<R>(
    val data: R,
    val included: Collection<JsonApiIncludedResource>? = null,
) where R : JsonApiResource

@Serializable
@Schema(name = "JsonApiDocumentList")
data class JsonApiDocumentList<R>(
    val data: Collection<R>,
    val included: Collection<JsonApiIncludedResource>? = null,
    val meta: JsonApiMeta,
) where R : JsonApiResource

interface JsonApiResource {
    val id: String
    val type: String
    val attributes: JsonApiResourceAttributes
    val relationships: Map<String, JsonApiRelationshipData>?

    fun findAllRelationshipWithType(type: String) = this.relationships?.flatMap { it.value.data }?.filter { it.type == type } ?: emptyList()
}

interface JsonApiResourceAttributes

@Serializable
@Schema(name = "JsonApiRelationshipData")
data class JsonApiRelationshipData(
    @field:ArraySchema(contains = Schema(implementation = JsonApiRelationshipIdentifier::class))
    val data: List<JsonApiRelationshipIdentifier>,
)

@Serializable
@Schema(name = "JsonApiRelationshipIdentifier")
data class JsonApiRelationshipIdentifier(
    @field:Schema(description = "resource id", example = "00000000-0000-0000-0000-000000000000")
    val id: String,
    @field:Schema(description = "resource type", example = "book")
    val type: String,
)

// TODO this should be deleted and JsonApiDocument should use the JsonApiResource interface
// Until kotlin serializer does not allow to hide classDiscriminator, this won't pass jsonapi schema validator
@Serializable
@Schema(name = "JsonApiIncludedResource")
data class JsonApiIncludedResource(
    @field:Schema(description = "resource id", example = "00000000-0000-0000-0000-000000000000")
    val id: String,
    @field:Schema(description = "resource type", example = "type")
    val type: String,
    val attributes: JsonApiResourceAttributes,
    // TODO should be enabled when an include has a relationship
    val relationships: Map<String, JsonApiRelationshipData>?,
)

fun JsonApiResource.toJsonApiIncludedResource() =
    JsonApiIncludedResource(
        id = id,
        type = type,
        attributes = attributes,
        relationships = relationships,
    )

@Serializable
@Schema(name = "JsonApiMeta")
data class JsonApiMeta(
    @field:Schema(description = "total responses", example = "1")
    val total: Int,
)

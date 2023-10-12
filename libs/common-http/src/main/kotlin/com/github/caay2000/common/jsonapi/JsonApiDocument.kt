package com.github.caay2000.common.jsonapi

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

interface JsonApiDocument {
    val data: JsonApiResource
    val included: List<JsonApiRelationshipResource>
}

interface JsonApiListDocument {
    val data: List<JsonApiResource>
    val meta: JsonApiMeta
}

interface JsonApiResource {
    val id: String?
    val type: String
    val attributes: JsonApiResourceAttributes
    val relationships: List<JsonApiRelationshipIdentifier>
}

interface JsonApiResourceAttributes

@Serializable
data class JsonApiRelationshipIdentifier(
    val id: String,
    val type: String
)

@Serializable
data class JsonApiRelationshipResource(
    val id: String,
    val type: String,
    @Polymorphic
    val attributes: JsonApiResourceAttributes
)

@Serializable
data class JsonApiMeta(val total: Int)


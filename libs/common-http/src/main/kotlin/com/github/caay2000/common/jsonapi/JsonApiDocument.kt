package com.github.caay2000.common.jsonapi

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
    val relationships: List<JsonApiRelationshipResource>
}

interface JsonApiResourceAttributes

interface JsonApiRelationshipResource {
    val id: String?
    val type: String
    val attributes: List<JsonApiResourceAttributes>
}

@Serializable
sealed interface JsonApiMeta {
    val total: Int
}

@Serializable
data class JsonApiMetaValue(override val total: Int) : JsonApiMeta

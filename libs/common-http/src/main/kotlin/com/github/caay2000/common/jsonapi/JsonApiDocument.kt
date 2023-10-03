package com.github.caay2000.common.jsonapi

interface JsonApiDocument {
    val data: JsonApiResource
    val included: List<JsonApiRelationshipResource>
}

interface JsonApiListDocument {
    val data: List<JsonApiResource>
}

interface JsonApiResource {
    val id: String?
    val type: String
    val attributes: JsonApiAttributes
    val relationships: List<JsonApiRelationshipResource>
}

interface JsonApiAttributes

interface JsonApiRelationshipResource {
    val id: String?
    val type: String
    val attributes: List<JsonApiAttributes>
}

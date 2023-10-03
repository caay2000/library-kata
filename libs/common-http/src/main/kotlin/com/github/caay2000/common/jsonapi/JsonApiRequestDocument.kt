package com.github.caay2000.common.jsonapi

interface JsonApiRequestDocument {
    val data: JsonApiRequestResource
}

interface JsonApiRequestResource {
    val type: String
    val attributes: JsonApiRequestAttributes
}

interface JsonApiRequestAttributes

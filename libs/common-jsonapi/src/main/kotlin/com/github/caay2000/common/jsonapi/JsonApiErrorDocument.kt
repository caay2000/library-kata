package com.github.caay2000.common.jsonapi

interface JsonApiErrorDocument {
    val errors: List<JsonApiErrorAttribute>
}

interface JsonApiErrorAttribute {
    val type: String
    val detail: String
}

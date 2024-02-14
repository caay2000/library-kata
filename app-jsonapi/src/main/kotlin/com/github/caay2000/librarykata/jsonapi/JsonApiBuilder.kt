package com.github.caay2000.librarykata.jsonapi

import com.github.caay2000.common.jsonapi.JsonApiDocument
import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiRequestParams
import com.github.caay2000.common.jsonapi.JsonApiResource

interface JsonApiBuilder<T : JsonApiResource> {
    fun getResource(
        identifier: String,
        params: JsonApiRequestParams = JsonApiRequestParams(),
    ): T

    fun getDocument(
        identifier: String,
        params: JsonApiRequestParams = JsonApiRequestParams(),
    ): JsonApiDocument<T>

    fun getDocumentList(
        params: JsonApiRequestParams,
    ): JsonApiDocumentList<T>
}

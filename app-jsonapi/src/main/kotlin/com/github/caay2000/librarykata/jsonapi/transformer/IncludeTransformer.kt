package com.github.caay2000.librarykata.jsonapi.transformer

import com.github.caay2000.common.http.Transformer
import com.github.caay2000.common.jsonapi.JsonApiIncludedResource
import com.github.caay2000.common.jsonapi.JsonApiResource

object IncludeTransformer : Transformer<Collection<JsonApiResource>, List<JsonApiIncludedResource>?> {
    fun invoke(value: JsonApiResource): JsonApiIncludedResource =
        JsonApiIncludedResource(
            id = value.id,
            type = value.type,
            attributes = value.attributes,
        )

    override fun invoke(
        value: Collection<JsonApiResource>,
        include: List<String>,
    ): List<JsonApiIncludedResource>? =
        if (value.isEmpty()) {
            null
        } else {
            value.map {
                JsonApiIncludedResource(
                    id = it.id,
                    type = it.type,
                    attributes = it.attributes,
                )
            }
        }
}

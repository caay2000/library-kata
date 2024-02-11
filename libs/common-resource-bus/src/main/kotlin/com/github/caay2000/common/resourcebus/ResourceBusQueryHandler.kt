package com.github.caay2000.common.resourcebus

import com.github.caay2000.common.jsonapi.JsonApiResource

interface ResourceBusQueryHandler<T : JsonApiResource> {
    fun retrieve(
        identifier: String,
        includeRelationships: Boolean,
    ): T
}

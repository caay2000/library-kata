package com.github.caay2000.common.query

import com.github.caay2000.common.jsonapi.JsonApiResource
import mu.KLogger

interface ResourceQueryHandler {
    val logger: KLogger

    val type: String

    fun invoke(query: ResourceQuery): ResourceQueryResponse {
        if (query.type != type) {
            throw IllegalArgumentException("invalid type ${query.type}")
        }
        logger.info { ">> processing $query" }
        return handle(query).also {
            logger.info { "<< response for $query: $it" }
        }
    }

    fun handle(query: ResourceQuery): ResourceQueryResponse
}

data class ResourceQuery(val identifier: String, val type: String)

data class ResourceQueryResponse(val resource: JsonApiResource)

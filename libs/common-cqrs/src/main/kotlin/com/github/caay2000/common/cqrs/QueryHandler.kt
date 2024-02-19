package com.github.caay2000.common.cqrs

import mu.KLogger

interface QueryHandler<Q : Query, R : QueryResponse> {
    val logger: KLogger

    fun invoke(query: Q): R {
        logger.info { ">> processing $query" }
        return handle(query).also {
            logger.info { "<< response for $query: $it" }
        }
    }

    fun handle(query: Q): R
}

interface Query

interface QueryResponse

package com.github.caay2000.common.cqrs

interface QueryHandler<Q : Query, R : QueryResponse> {

    fun handle(query: Q): R
}

interface Query
interface QueryResponse

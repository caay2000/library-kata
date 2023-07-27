package com.github.caay2000.projectskeleton.common.cqrs

interface QueryHandler<Q : Query, R : QueryResponse> {

    fun handle(query: Q): R
}

interface Query
interface QueryResponse

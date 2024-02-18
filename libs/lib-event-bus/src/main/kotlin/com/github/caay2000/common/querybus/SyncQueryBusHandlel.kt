package com.github.caay2000.common.querybus

import kotlin.reflect.KClass

class SyncQueryBusHandler {
    val handlers: MutableMap<String, QueryBusHandler<*, *>> = mutableMapOf()

    inline fun <reified Q : Query, R : QueryResponse> register(queryHandler: QueryBusHandler<Q, R>): SyncQueryBusHandler {
        handlers[Q::class.java.canonicalName] = queryHandler
        return this
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified Q : Query, R : QueryResponse> invoke(query: Q): R =
        (handlers.getOrElse(Q::class.java.canonicalName) { throw SyncQueryBusHandlerError.QueryBusHandlerNotFound(Q::class) } as QueryBusHandler<Q, R>)
            .invoke(query)
}

sealed class SyncQueryBusHandlerError(message: String) : RuntimeException(message) {
    data class QueryBusHandlerNotFound(val type: KClass<*>) : SyncQueryBusHandlerError("QueryBusHandler not found for type ${type::class.java.canonicalName}")
}

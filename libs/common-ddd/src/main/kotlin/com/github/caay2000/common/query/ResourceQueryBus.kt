package com.github.caay2000.common.query

class ResourceQueryBus {
    private val handlers: MutableMap<String, ResourceQueryHandler> = mutableMapOf()

    fun subscribe(queryHandler: ResourceQueryHandler) {
        handlers[queryHandler.type] = queryHandler
    }

    fun invoke(query: ResourceQuery): ResourceQueryResponse = handlers[query.type]?.invoke(query) ?: throw ResourceQueryBusError.ResourceQueryBusNotFound(query)
}

sealed class ResourceQueryBusError(message: String) : RuntimeException(message) {
    data class ResourceQueryBusNotFound(val query: ResourceQuery) : ResourceQueryBusError("ResourceQueryBus not found for type ${query.type}")
}

fun ResourceQueryBus.register(queryHandler: ResourceQueryHandler): ResourceQueryBus {
    this.subscribe(queryHandler)
    return this
}

package com.github.caay2000.common.resourcebus

import com.github.caay2000.common.jsonapi.JsonApiResource
import kotlin.reflect.KClass

class JsonApiResourceBus {
    val handlers: MutableMap<String, ResourceBusQueryHandler<*>> = mutableMapOf()

    inline fun <reified T : JsonApiResource> register(queryHandler: ResourceBusQueryHandler<T>): JsonApiResourceBus {
        handlers[T::class.java.canonicalName] = queryHandler
        return this
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : JsonApiResource> retrieve(
        identifier: String,
        includeRelationships: Boolean = true,
    ): T =
        handlers.getOrElse(T::class.java.canonicalName) { throw JsonApiResourceBusError.ResourceHandlerNotFound(T::class) }
            .retrieve(identifier, includeRelationships) as T
}

sealed class JsonApiResourceBusError(message: String) : RuntimeException(message) {
    data class ResourceHandlerNotFound(val type: KClass<*>) : JsonApiResourceBusError("ResourceHandler not found for type ${type::class.java.canonicalName}")
}

//package com.github.caay2000.common.query
//
//import com.github.caay2000.common.cqrs.Query
//import com.github.caay2000.common.cqrs.QueryHandler
//import com.github.caay2000.common.cqrs.QueryResponse
//
//
//class DomainQueryBus {
//
//    val handlers: MutableMap<String, QueryHandler<*, *>> = mutableMapOf()
//
//    inline fun <reified Q : Query, R : QueryResponse> register(queryHandler: QueryHandler<Q, R>) {
//        handlers[Q::class.java.canonicalName] = queryHandler
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    inline fun <reified Q : Query, R : QueryResponse> invoke(query: Q): R =
//        (handlers.getOrElse(Q::class.java.canonicalName) { throw SyncQueryBusHandlerError.QueryBusHandlerNotFound(Q::class) } as QueryBusHandler<Q, R>)
//            .invoke(query)
//
//}
//
//inline fun <reified Q : Query, R : QueryResponse> DomainQueryBus.register(queryHandler: QueryHandler<Q, R>): DomainQueryBus {
//    this.register(queryHandler)
//    return this
//}
//
//

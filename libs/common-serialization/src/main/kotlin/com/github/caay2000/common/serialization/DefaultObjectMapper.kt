package com.github.caay2000.common.serialization

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper


fun ObjectMapper.defaultJacksonConfiguration(block: ObjectMapper.() -> Unit = {}) {
    configure(SerializationFeature.INDENT_OUTPUT, true)
    registerModule(JavaTimeModule())
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

//    val ptv = BasicPolymorphicTypeValidator.builder()
//        .allowIfSubType("om.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.serialization.LoanDocument")
//        .allowIfSubType("java.util.ArrayList")
//        .build()
//
    activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS, JsonTypeInfo.As.PROPERTY)
//    deactivateDefaultTyping()

//    enableDefaultTyping(ObjectMapper.DefaultTyping.NON_CONCRETE_AND_ARRAYS)
//
//    activateDefaultTyping(, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, JsonTypeInfo.As.PROPERTY)


    apply { block() }
}

fun defaultObjectMapper() = jacksonObjectMapper().apply { defaultJacksonConfiguration() }


package com.github.caay2000.common.http

interface RequestInclude

fun List<String>?.shouldProcess(include: String): Boolean = this?.let { map { it.uppercase() }.contains(include.uppercase()) } ?: false

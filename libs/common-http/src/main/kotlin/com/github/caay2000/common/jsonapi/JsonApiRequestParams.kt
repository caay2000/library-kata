package com.github.caay2000.common.jsonapi

data class JsonApiRequestParams(
    val filter: Map<String, List<String>> = mutableMapOf(),
    val sort: Map<String, String> = mutableMapOf(),
    val pageNumber: Int = 1,
    val pageSize: Int = 10,
    val include: List<String> = listOf(),
) {
    companion object {
        private const val FILTER_TYPE = "type"
    }

    val type: List<String>?
        get() = filter[FILTER_TYPE]
}

@JvmName("flattenToJsonApiRequestParams")
fun Map<String, List<String>>.toJsonApiRequestParams(): JsonApiRequestParams =
    this.mapValues {
        if (it.value.size != 1) {
            throw IllegalJsonApiRequestParamException("invalid values for ${it.key}")
        } else {
            it.value.first()
        }
    }.toJsonApiRequestParams()

private fun Map<String, String>.toJsonApiRequestParams(): JsonApiRequestParams {
    val includes = mutableListOf<String>()
    val filterRegex = Regex("filter\\[(.*)]")
    val sort: MutableMap<String, String> = mutableMapOf()
    val filter: MutableMap<String, List<String>> = mutableMapOf()
    var pageNumber = 1
    var pageSize = 10

    this.entries.forEach { entry ->
        val filterMatches: List<String> = filterRegex.find(entry.key)?.groupValues ?: listOf()
        when (entry.key) {
            "include" -> entry.value.split(",").map { includes.add(it.uppercase().trimIndent()) }
            "page[number]" -> pageNumber = entry.value.toInt()
            "page[size]" -> pageSize = entry.value.toInt()
            "sort" ->
                entry.value.split(",")
                    .forEach { field ->
                        if (field.startsWith("-")) sort[field.substring(1)] = "desc" else sort[field] = "asc"
                    }

            in filterMatches -> filter[filterMatches[1]] = entry.value.split(",")
        }
    }
    return JsonApiRequestParams(sort = sort.toMap(), filter = filter.toMap(), pageNumber = pageNumber, pageSize = pageSize, include = includes)
}

class IllegalJsonApiRequestParamException(override val message: String) : RuntimeException(message)

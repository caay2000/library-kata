package com.github.caay2000.dikt

import com.github.caay2000.dikt.DiKtException.BeanNotFound
import com.github.caay2000.dikt.DiKtException.MultipleBeansFound
import mu.KLogger
import mu.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

object DiKt {
    private val logger: KLogger = KotlinLogging.logger {}
    private val context: Context = Context()

    fun register(
        override: Boolean = false,
        block: () -> Any,
    ) {
        try {
            val bean = block()
            register(bean::class.qualifiedName.toString(), override) { bean }
        } catch (e: DiKtException) {
            logger.warn("cannot instantiate bean: ${e.message}")
            throw e
        }
    }

    fun register(
        name: String,
        override: Boolean = false,
        block: () -> Any,
    ) {
        try {
            val bean = block()
            context.registerBean(name, override, bean)
            logger.debug("'$name' registered correctly")
        } catch (e: RuntimeException) {
            logger.warn("cannot instantiate bean '$name': ${e.message}")
            throw e
        }
    }

    fun <T> get(name: String): T = context.getBean(name)

    inline fun <reified T : Any> get(): T = bind(T::class)

    inline fun <reified T : Any> bind(): T = bind(T::class)

    fun <T> bind(clazz: KClass<*>): T = context.bind(clazz)

    fun <T : Any> bind(name: String): T = context.getBean(name)

    fun clear() = context.clear()

    internal class Context {
        private val context: MutableMap<BeanContext, Any> = mutableMapOf()

        internal fun registerBean(
            name: String,
            override: Boolean,
            bean: Any,
        ) {
            if (context.keys.count { it.name == name } == 0) {
                context[bean.toContext(name, override)] = bean
            } else {
                if (override) {
                    context[bean.toContext(name, false)] = bean
                } else {
                    throw DiKtException.BeanAlreadyExists(name)
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        internal fun <T> getBean(name: String): T = findBeanByName(name) as T

        @Suppress("UNCHECKED_CAST")
        internal fun <T> bind(clazz: KClass<*>): T = findBeanByType(clazz.createType()) as T

        fun clear() = this.context.clear()

        private fun findBeanByType(type: KType): Any {
            val items = context.keys.filter { it.type == type.toString() || it.interfaces.contains(type.toString()) }
            return when {
                items.isEmpty() -> {
                    logger.warn { "unable to find bean with type '$type'" }
                    throw BeanNotFound(type.toString())
                }

                items.count { it.override } == 1 -> context[items.filter { it.override }[0]]!!

                items.size > 1 -> {
                    logger.warn { "multiple beans found with type '$type'" }
                    throw MultipleBeansFound(type.toString())
                }

                else -> context[items[0]]!!
            }
        }

        private fun findBeanByName(name: String): Any {
            val items = context.keys.filter { it.name == name }
            return when {
                items.isEmpty() -> {
                    logger.warn { "unable to find bean with name '$name'" }
                    throw BeanNotFound(name)
                }

                else -> context[items[0]]!!
            }
        }

        private data class BeanContext(
            val name: String,
            val type: String,
            val override: Boolean,
            val interfaces: List<String>,
        )

        private fun Any.toContext(
            name: String,
            override: Boolean,
        ): BeanContext =
            BeanContext(
                name = name,
                type = this::class.qualifiedName!!,
                override = override,
                interfaces = this::class.java.interfaces.map { it.canonicalName },
            )
    }
}

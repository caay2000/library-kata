package com.github.caay2000.dikt

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class DiKtTest {
    @BeforeEach
    fun setUp() {
        DiKt.clear()
    }

    @Test
    fun `should register a simple bean`() {
        val bean = SimpleBean()
        DiKt.register { bean }

        assertThat(DiKt.get<SimpleBean>())
            .isEqualTo(bean)
    }

    @Test
    fun `should register a simple bean with name`() {
        val bean = SimpleBean()
        DiKt.register("simpleBean") { bean }

        assertThat(DiKt.get<SimpleBean>("simpleBean"))
            .isEqualTo(bean)
    }

    @Test
    fun `should register a simple bean with block`() {
        val bean = SimpleBean()
        DiKt.register("simpleBean") { bean.update() }

        assertThat(DiKt.get<SimpleBean>("simpleBean"))
            .isEqualTo(bean.update())
    }

    @Test
    fun `should retrieve a bean via its class`() {
        val bean = InterfaceBean()
        DiKt.register { bean }

        assertThat(DiKt.get<InterfaceBean>())
            .isEqualTo(bean)
    }

    @Test
    fun `should retrieve a bean via its interface`() {
        val bean = InterfaceBean()
        DiKt.register { bean }

        assertThat(DiKt.get<SimpleInterface>())
            .isEqualTo(bean)
    }

    @Test
    fun `should register a complex bean`() {
        val interfaceBean = InterfaceBean()
        DiKt.register("interfaceBean") { interfaceBean }
        DiKt.register { InterfaceDependencyBean(DiKt.bind()) }

        assertThat(DiKt.get<InterfaceDependencyBean>())
            .isEqualTo(InterfaceDependencyBean(interfaceBean))
    }

    @Test
    fun `should register a complex bean with named bind`() {
        val interfaceBean = InterfaceBean()
        DiKt.register("interfaceBean") { interfaceBean }
        DiKt.register("interfaceBean2") { InterfaceBean() }
        DiKt.register { InterfaceDependencyBean(DiKt.bind("interfaceBean")) }

        assertThat(DiKt.get<InterfaceDependencyBean>())
            .isEqualTo(InterfaceDependencyBean(interfaceBean))
    }

    @Test
    fun `should register a complex bean with named bind and override`() {
        val interfaceBean = InterfaceBean()
        DiKt.register("interfaceBean", override = true) { interfaceBean }
        DiKt.register("interfaceBean2") { interfaceBean }
        DiKt.register { InterfaceDependencyBean(DiKt.bind()) }

        assertThat(DiKt.get<InterfaceDependencyBean>())
            .isEqualTo(InterfaceDependencyBean(interfaceBean))
    }

    @Test
    fun `should fail if 2 beans has the same name`() {
        Assertions.assertThatThrownBy {
            DiKt.register("sameName") { "whatever" }
            DiKt.register("sameName") { "whatever 2" }
        }.isInstanceOf(DiKtException.BeanAlreadyExists::class.java)
    }

    @Test
    fun `should fail when bind returns more than 1 candidate`() {
        Assertions.assertThatThrownBy {
            val interfaceBean = InterfaceBean()
            DiKt.register("interfaceBean") { interfaceBean }
            DiKt.register("interfaceBean2") { InterfaceBean() }
            DiKt.register { (InterfaceDependencyBean(DiKt.bind())) }
        }.isInstanceOf(DiKtException.MultipleBeansFound::class.java)
    }

    @Test
    fun `should fail if a bean does not exists by type`() {
        Assertions.assertThatThrownBy {
            DiKt.get<SimpleBean>()
        }.isInstanceOf(DiKtException.BeanNotFound::class.java)
    }

    @Test
    fun `should fail if a bean does not exists by name`() {
        Assertions.assertThatThrownBy {
            DiKt.get<SimpleBean>("not found bean")
        }.isInstanceOf(DiKtException.BeanNotFound::class.java)
    }

    data class SimpleBean(val aux: LocalDateTime = LocalDateTime.now())

    private fun SimpleBean.update(): SimpleBean = this.copy(aux = this.aux.plusDays(1))

    interface SimpleInterface

    data class InterfaceBean(val aux: LocalDateTime = LocalDateTime.now()) : SimpleInterface

    data class InterfaceDependencyBean(val dep: SimpleInterface)
}

package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.intellij.lang.annotations.Language
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SpecifyFunctionExplicitReturnTypeTest {

    @Test
    fun publicFun_passed() {
        assertRulePassed(
            isPublicEnabled = true,
            content = "fun foo(): Int = 5"
        )
    }

    @Test
    fun publicFun_failed() {
        assertRuleFailed(
            isPublicEnabled = true,
            content = "fun foo() = 5"
        )
    }

    @Test
    fun publicFun_disabled() {
        assertRulePassed(
            content = "fun foo() = 5"
        )
    }

    @Test
    fun internalFun_passed() {
        assertRulePassed(
            isInternalEnabled = true,
            content = "internal fun foo(): Int = 5"
        )
    }

    @Test
    fun internalFun_failed() {
        assertRuleFailed(
            isInternalEnabled = true,
            content = "internal fun foo() = 5"
        )
    }

    @Test
    fun internalFun_disabled() {
        assertRulePassed(
            content = "internal fun foo() = 5"
        )
    }

    @Test
    fun protectedFun_passed() {
        assertRulePassed(
            isProtectedEnabled = true,
            content = "protected fun foo(): Int = 5"
        )
    }

    @Test
    fun protectedFun_failed() {
        assertRuleFailed(
            isProtectedEnabled = true,
            content = "protected fun foo() = 5"
        )
    }

    @Test
    fun protectedFun_disabled() {
        assertRulePassed(
            content = "protected fun foo() = 5"
        )
    }

    @Test
    fun privateFun_passed() {
        assertRulePassed(
            isPrivateEnabled = true,
            content = "private fun foo(): Int = 5"
        )
    }

    @Test
    fun privateFun_failed() {
        assertRuleFailed(
            isPrivateEnabled = true,
            content = "private fun foo() = 5"
        )
    }

    @Test
    fun privateFun_disabled() {
        assertRulePassed(
            content = "private fun foo() = 5"
        )
    }

    @Test
    fun publicFun_classPublicMatch() {
        assertRuleFailed(
            isPublicEnabled = true,
            content = """
                object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun publicFun_classPublicUnmatch() {
        assertRulePassed(
            isPublicEnabled = true,
            content = """
                internal object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun publicFun_classInternalMatch() {
        assertRuleFailed(
            isInternalEnabled = true,
            content = """
                internal object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun publicFun_classInternalUnmatch() {
        assertRulePassed(
            isInternalEnabled = true,
            content = """
                private object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun publicFun_classPrivateMatch() {
        assertRuleFailed(
            isPrivateEnabled = true,
            content = """
                private object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun publicFun_classPrivateUnmatch() {
        assertRulePassed(
            isPrivateEnabled = true,
            content = """
                internal object A {
                    fun foo() = 5 
                }
            """
        )
    }

    private fun assertRuleFailed(
        isPublicEnabled: Boolean = false,
        isInternalEnabled: Boolean = false,
        isProtectedEnabled: Boolean = false,
        isPrivateEnabled: Boolean = false,
        @Language("kotlin") content: String
    ) {
        val config = DeclarationExplicitReturnTypeTestConfig(
            isPublicEnabled = isPublicEnabled,
            isInternalEnabled = isInternalEnabled,
            isProtectedEnabled = isProtectedEnabled,
            isPrivateEnabled = isPrivateEnabled
        )
        assertFalse(compileRule(config, content).isEmpty())
    }

    private fun assertRulePassed(
        isPublicEnabled: Boolean = false,
        isInternalEnabled: Boolean = false,
        isProtectedEnabled: Boolean = false,
        isPrivateEnabled: Boolean = false,
        @Language("kotlin") content: String
    ) {
        val config = DeclarationExplicitReturnTypeTestConfig(
            isPublicEnabled = isPublicEnabled,
            isInternalEnabled = isInternalEnabled,
            isProtectedEnabled = isProtectedEnabled,
            isPrivateEnabled = isPrivateEnabled
        )
        assertTrue(compileRule(config, content).isEmpty())
    }

    private fun compileRule(config: Config, @Language("kotlin") content: String): List<Finding> {
        return SpecifyFunctionExplicitReturnType(config).compileAndLint(content)
    }
}
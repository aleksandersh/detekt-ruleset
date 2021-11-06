package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.intellij.lang.annotations.Language
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SpecifyPropertyExplicitReturnTypeTest {

    @Test
    fun publicProperty_passed() {
        assertRulePassed(
            isPublicEnabled = true,
            content = "val bar: Int = 5"
        )
    }

    @Test
    fun publicProperty_failed() {
        assertRuleFailed(
            isPublicEnabled = true,
            content = "val bar = 5"
        )
    }

    @Test
    fun publicProperty_disabled() {
        assertRulePassed(
            content = "val bar = 5"
        )
    }

    @Test
    fun internalProperty_passed() {
        assertRulePassed(
            isInternalEnabled = true,
            content = "internal val bar: Int = 5"
        )
    }

    @Test
    fun internalProperty_failed() {
        assertRuleFailed(
            isInternalEnabled = true,
            content = "internal val bar = 5"
        )
    }

    @Test
    fun internalProperty_disabled() {
        assertRulePassed(
            content = "internal val bar = 5"
        )
    }

    @Test
    fun protectedProperty_passed() {
        assertRulePassed(
            isProtectedEnabled = true,
            content = "protected val bar: Int = 5"
        )
    }

    @Test
    fun protectedProperty_failed() {
        assertRuleFailed(
            isProtectedEnabled = true,
            content = "protected val bar = 5"
        )
    }

    @Test
    fun protectedProperty_disabled() {
        assertRulePassed(
            content = "protected val bar = 5"
        )
    }

    @Test
    fun privateProperty_passed() {
        assertRulePassed(
            isPrivateEnabled = true,
            content = "private val bar: Int = 5"
        )
    }

    @Test
    fun privateProperty_failed() {
        assertRuleFailed(
            isPrivateEnabled = true,
            content = "private val bar = 5"
        )
    }

    @Test
    fun privateProperty_disabled() {
        assertRulePassed(
            content = "private val bar = 5"
        )
    }

    @Test
    fun publicFun_classInternalUnmatch() {
        assertRulePassed(
            isInternalEnabled = true,
            content = """
                private object A {
                    val bar = 5 
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
                    val bar = 5 
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
                    val bar = 5 
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
        return SpecifyPropertyExplicitReturnType(config).compileAndLint(content)
    }
}
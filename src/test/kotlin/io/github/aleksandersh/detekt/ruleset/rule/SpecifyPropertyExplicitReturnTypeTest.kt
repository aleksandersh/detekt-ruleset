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
    fun val_passed() {
        assertRulePassed(
            "val bar: Int = 5"
        )
    }

    @Test
    fun val_failed() {
        assertRuleFailed(
            "val bar = 5"
        )
    }

    @Test
    fun class_passed() {
        assertRulePassed(
            """
                object A {
                    val bar: Int = 5
                }
            """
        )
    }

    @Test
    fun class_failed() {
        assertRuleFailed(
            """
                object A {
                    val bar = 5
                }
            """
        )
    }

    private val declarationExplicitReturnTypeTestConfig = DeclarationExplicitReturnTypeTestConfig(true)

    private fun assertRuleFailed(@Language("kotlin") code: String) {
        assertFalse(compileRule(declarationExplicitReturnTypeTestConfig, code).isEmpty())
    }

    private fun assertRulePassed(@Language("kotlin") code: String) {
        assertTrue(compileRule(declarationExplicitReturnTypeTestConfig, code).isEmpty())
    }

    private fun compileRule(config: Config, @Language("kotlin") code: String): List<Finding> {
        return SpecifyPropertyExplicitReturnType(config).compileAndLint(code)
    }
}
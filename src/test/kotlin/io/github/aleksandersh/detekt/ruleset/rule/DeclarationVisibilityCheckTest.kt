package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DeclarationVisibilityCheckTest {

    @Test
    fun matchPublic_publicFun() {
        assertRuleMatched(
            isPublicEnabled = true,
            code = "fun foo() = 5"
        )
    }

    @Test
    fun matchPublic_internalFun() {
        assertRuleUnmatched(
            isPublicEnabled = true,
            code = "internal fun foo() = 5"
        )
    }

    @Test
    fun matchPublic_privateFun() {
        assertRuleUnmatched(
            isPublicEnabled = true,
            code = "private fun foo() = 5"
        )
    }

    @Test
    fun matchPublic_publicClass() {
        assertRuleMatched(
            isPublicEnabled = true,
            code = """
                object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPublic_publicClassProtectedFun() {
        assertRuleUnmatched(
            isPublicEnabled = true,
            code = """
                abstract class A {
                    protected fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPublic_internalClass() {
        assertRuleUnmatched(
            isPublicEnabled = true,
            code = """
                internal object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPublic_privateClass() {
        assertRuleUnmatched(
            isPublicEnabled = true,
            code = """
                private object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPublic_disabled() {
        assertRuleUnmatched(
            code = "fun foo() = 5"
        )
    }

    @Test
    fun matchInternal_publicFun() {
        assertRuleUnmatched(
            isInternalEnabled = true,
            code = "fun foo() = 5"
        )
    }

    @Test
    fun matchInternal_internalFun() {
        assertRuleMatched(
            isInternalEnabled = true,
            code = "internal fun foo() = 5"
        )
    }

    @Test
    fun matchInternal_privateFun() {
        assertRuleUnmatched(
            isInternalEnabled = true,
            code = "private fun foo() = 5"
        )
    }

    @Test
    fun matchInternal_publicClass() {
        assertRuleUnmatched(
            isInternalEnabled = true,
            code = """
                object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchInternal_internalClassProtectedFun() {
        assertRuleUnmatched(
            isInternalEnabled = true,
            code = """
                internal abstract class A {
                    protected fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchInternal_internalClass() {
        assertRuleMatched(
            isInternalEnabled = true,
            code = """
                internal object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchInternal_privateClass() {
        assertRuleUnmatched(
            isInternalEnabled = true,
            code = """
                private object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchInternal_disabled() {
        assertRuleUnmatched(
            code = "internal fun foo() = 5"
        )
    }

    @Test
    fun matchProtected_protectedFun() {
        assertRuleUnmatched(
            isPublicEnabled = true,
            code = """
                abstract class A {
                    protected fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPrivate_publicFun() {
        assertRuleMatched(
            isPrivateEnabled = true,
            code = "fun foo() = 5"
        )
    }

    @Test
    fun matchPrivate_internalFun() {
        assertRuleMatched(
            isPrivateEnabled = true,
            code = "internal fun foo() = 5"
        )
    }

    @Test
    fun matchPrivate_privateFun() {
        assertRuleMatched(
            isPrivateEnabled = true,
            code = "private fun foo() = 5"
        )
    }

    @Test
    fun matchPrivate_publicClass() {
        assertRuleUnmatched(
            isPrivateEnabled = true,
            code = """
                object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPrivate_privateClassProtectedFun() {
        assertRuleMatched(
            isPrivateEnabled = true,
            code = """
                private abstract class A {
                    protected fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPrivate_internalClass() {
        assertRuleUnmatched(
            isPrivateEnabled = true,
            code = """
                internal object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPrivate_privateClass() {
        assertRuleMatched(
            isPrivateEnabled = true,
            code = """
                private object A {
                    fun foo() = 5 
                }
            """
        )
    }

    @Test
    fun matchPrivate_disabled() {
        assertRuleUnmatched(
            code = "private fun foo() = 5"
        )
    }

    private fun assertRuleUnmatched(
        isPublicEnabled: Boolean = false,
        isInternalEnabled: Boolean = false,
        isProtectedEnabled: Boolean = false,
        isPrivateEnabled: Boolean = false,
        @Language("kotlin") code: String
    ) {
        val config = DeclarationExplicitReturnTypeTestConfig(
            isPublicEnabled = isPublicEnabled,
            isInternalEnabled = isInternalEnabled,
            isProtectedEnabled = isProtectedEnabled,
            isPrivateEnabled = isPrivateEnabled
        )
        assertTrue(compileRule(config, code).isEmpty())
    }

    private fun assertRuleMatched(
        isPublicEnabled: Boolean = false,
        isInternalEnabled: Boolean = false,
        isProtectedEnabled: Boolean = false,
        isPrivateEnabled: Boolean = false,
        @Language("kotlin") code: String
    ) {
        val config = DeclarationExplicitReturnTypeTestConfig(
            isPublicEnabled = isPublicEnabled,
            isInternalEnabled = isInternalEnabled,
            isProtectedEnabled = isProtectedEnabled,
            isPrivateEnabled = isPrivateEnabled
        )
        assertFalse(compileRule(config, code).isEmpty())
    }

    private fun compileRule(config: Config, @Language("kotlin") code: String): List<Finding> {
        return TestRule(config).compileAndLint(code)
    }

    private class TestRule(config: Config) : Rule(config) {

        override val issue: Issue = Issue("TestIssue", Severity.CodeSmell, "Test description", Debt.TEN_MINS)

        private val declarationVisibilityCheck = DeclarationVisibilityCheck(this)

        override fun visitNamedFunction(declaration: KtNamedFunction) {
            if (declarationVisibilityCheck.checkVisibility(declaration)) {
                report(CodeSmell(issue, Entity.from(declaration), "Visibility matched"))
            }
        }
    }
}
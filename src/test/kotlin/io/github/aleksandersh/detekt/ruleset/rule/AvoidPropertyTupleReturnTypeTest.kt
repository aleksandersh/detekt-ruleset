package io.github.aleksandersh.detekt.ruleset.rule

import io.github.detekt.test.utils.KotlinCoreEnvironmentWrapper
import io.github.detekt.test.utils.createEnvironment
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.test.compileAndLintWithContext
import org.intellij.lang.annotations.Language
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class AvoidPropertyTupleReturnTypeTest {

    companion object {

        private val env get() = envWrapper.env

        private lateinit var envWrapper: KotlinCoreEnvironmentWrapper

        @BeforeClass
        @JvmStatic
        fun setUpBeforeClass() {
            envWrapper = createEnvironment()
        }

        @AfterClass
        @JvmStatic
        fun tearDownAfterClass() {
            envWrapper.dispose()
        }
    }

    @Test
    fun pairValue() {
        assertRuleFailed(
            "val foo: Pair<Int, Int> = 1 to 2"
        )
    }

    @Test
    fun pairVariable() {
        assertRuleFailed(
            "var foo: Pair<Int, Int> = 1 to 2"
        )
    }

    @Test
    fun triple() {
        Triple(1, 2, 3)
        assertRuleFailed(
            "val foo: Triple<Int, Int, Int> = Triple(1, 2, 3)"
        )
    }

    @Test
    fun intValue() {
        assertRulePassed(
            "val foo: Int = 1"
        )
    }

    @Test
    fun intVariable() {
        assertRulePassed(
            "var foo: Int = 1"
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
        return AvoidPropertyTupleReturnType(config).compileAndLintWithContext(env, code)
    }
}
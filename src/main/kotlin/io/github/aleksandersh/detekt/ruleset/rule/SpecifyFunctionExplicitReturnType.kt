package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject

class SpecifyFunctionExplicitReturnType(config: Config = Config.empty) : Rule(config) {

    companion object {

        const val RULE_ID = "SpecifyFunctionExplicitReturnType"
    }

    override val issue: Issue = createIssue()

    private val declarationExplicitReturnType = DeclarationExplicitReturnType(this)

    override fun visitNamedFunction(function: KtNamedFunction) {
        if (!function.isLocal &&
            !isClassLocal(function) &&
            hasExpressionBodyWithoutExplicitReturnType(function)
        ) {
            declarationExplicitReturnType.visitDeclaration(function)
        }
        super.visitNamedFunction(function)
    }

    private fun isClassLocal(declaration: KtNamedDeclaration): Boolean {
        return declaration.containingClassOrObject?.isLocal ?: return false
    }

    private fun hasExpressionBodyWithoutExplicitReturnType(function: KtNamedFunction): Boolean {
        return function.equalsToken != null && !function.hasDeclaredReturnType()
    }

    private fun createIssue(): Issue {
        val description = "Functions should have an explicit return type. " +
                "Inferred return type can easily be changed by mistake which may lead to breaking changes."
        return Issue(RULE_ID, Severity.CodeSmell, description, Debt(0, 0, 1))
    }
}
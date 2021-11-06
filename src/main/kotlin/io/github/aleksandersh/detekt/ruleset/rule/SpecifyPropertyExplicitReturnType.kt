package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.rules.isConstant
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject

class SpecifyPropertyExplicitReturnType(config: Config = Config.empty) : Rule(config) {

    companion object {

        const val RULE_ID = "SpecifyPropertyExplicitReturnType"
    }

    override val issue: Issue = createIssue()

    private val declarationExplicitReturnType = DeclarationExplicitReturnType(this)

    override fun visitProperty(property: KtProperty) {
        if (!property.isConstant() &&
            !property.isLocal &&
            !isClassLocal(property) &&
            hasExpressionBodyWithoutExplicitReturnType(property)
        ) {
            declarationExplicitReturnType.visitDeclaration(property)
        }
        super.visitProperty(property)
    }

    private fun isClassLocal(declaration: KtNamedDeclaration): Boolean {
        return declaration.containingClassOrObject?.isLocal ?: return false
    }

    private fun hasExpressionBodyWithoutExplicitReturnType(property: KtProperty): Boolean {
        return property.typeReference == null
    }

    private fun createIssue(): Issue {
        val description = "Properties should have an explicit return type. " +
                "Inferred return type can easily be changed by mistake which may lead to breaking changes."
        return Issue(RULE_ID, Severity.CodeSmell, description, Debt(0, 0, 1))
    }
}
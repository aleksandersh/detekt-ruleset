package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.api.internal.RequiresTypeResolution
import io.gitlab.arturbosch.detekt.rules.fqNameOrNull
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.resolve.BindingContext

@RequiresTypeResolution
class AvoidPropertyTupleReturnType(config: Config = Config.empty) : Rule(config) {

    companion object {

        const val RULE_ID = "AvoidPropertyTupleReturnType"
    }

    override val issue: Issue = createIssue()

    private val declarationVisibilityCheck = DeclarationVisibilityCheck(this)

    override fun visitProperty(property: KtProperty) {
        if (bindingContext == BindingContext.EMPTY) return
        val returnType = bindingContext[BindingContext.VARIABLE, property]?.returnType ?: return
        val typeName = returnType.fqNameOrNull()?.asString() ?: return
        if (isTuple(typeName) && declarationVisibilityCheck.checkVisibility(property)) {
            reportCodeSmell(property, typeName)
        }
    }

    private fun isTuple(type: String): Boolean {
        return type == "kotlin.Pair" || type == "kotlin.Triple"
    }

    private fun createIssue(): Issue {
        val description = "Properties should have an explicit return type instead of tuple."
        return Issue(RULE_ID, Severity.CodeSmell, description, Debt(0, 0, 10))
    }

    private fun reportCodeSmell(declaration: KtNamedDeclaration, tuple: String) {
        val message = "Property '${declaration.nameAsSafeName}' has an tuple return type ($tuple)."
        report(CodeSmell(issue, Entity.atName(declaration), message))
    }
}
package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.rules.isInternal
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.psi.psiUtil.isProtected
import org.jetbrains.kotlin.psi.psiUtil.isPublic
import java.util.*

internal class DeclarationExplicitReturnType(private val rule: Rule) {

    companion object {

        const val KEY_CHECK_PUBLIC = "checkPublic"
        const val KEY_CHECK_INTERNAL = "checkInternal"
        const val KEY_CHECK_PROTECTED = "checkProtected"
        const val KEY_CHECK_PRIVATE = "checkPrivate"
    }

    private val isPublicCheckEnabled = getBoolean(KEY_CHECK_PUBLIC)
    private val isInternalCheckEnabled = getBoolean(KEY_CHECK_INTERNAL)
    private val isProtectedCheckEnabled = getBoolean(KEY_CHECK_PROTECTED)
    private val isPrivateCheckEnabled = getBoolean(KEY_CHECK_PRIVATE)

    fun visitDeclaration(declaration: KtNamedDeclaration) {
        val classOrObject = declaration.containingClassOrObject
        when {
            isPublicCheckEnabled && isPublic(declaration, classOrObject) -> {
                reportCodeSmell(declaration, "Public")
            }
            isInternalCheckEnabled && isInternal(declaration, classOrObject) -> {
                reportCodeSmell(declaration, "Internal")
            }
            isProtectedCheckEnabled && declaration.isProtected() -> {
                reportCodeSmell(declaration, "Protected")
            }
            isPrivateCheckEnabled && isPrivate(declaration, classOrObject) -> {
                reportCodeSmell(declaration, "private")
            }
        }
    }

    private fun isPublic(declaration: KtNamedDeclaration, classOrObject: KtClassOrObject?): Boolean {
        return declaration.isPublic && (classOrObject == null || classOrObject.isPublic)
    }

    private fun isInternal(declaration: KtNamedDeclaration, classOrObject: KtClassOrObject?): Boolean {
        return isAboveInternal(declaration) && (classOrObject == null || isAboveInternal(classOrObject))
    }

    private fun isAboveInternal(declaration: KtNamedDeclaration): Boolean {
        return declaration.isInternal() || declaration.isPublic
    }

    private fun isPrivate(declaration: KtNamedDeclaration, classOrObject: KtClassOrObject?): Boolean {
        return declaration.isPrivate() || (classOrObject == null || classOrObject.isPrivate())
    }

    private fun reportCodeSmell(function: KtNamedDeclaration, modifier: String) {
        val capitalizedModifier =
            modifier.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val message = "$capitalizedModifier declaration '${function.nameAsSafeName}' without explicit return type."
        rule.report(CodeSmell(rule.issue, Entity.from(function), message))
    }

    private fun getBoolean(key: String): Boolean {
        return rule.valueOrDefault(key, true)
    }
}
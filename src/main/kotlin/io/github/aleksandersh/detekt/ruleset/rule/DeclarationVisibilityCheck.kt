package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.rules.isInternal
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtNamedDeclaration
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.isPrivate
import org.jetbrains.kotlin.psi.psiUtil.isProtected
import org.jetbrains.kotlin.psi.psiUtil.isPublic

internal class DeclarationVisibilityCheck(private val rule: Rule) {

    companion object {

        const val KEY_CHECK_PUBLIC = "checkPublic"
        const val KEY_CHECK_INTERNAL = "checkInternal"
        const val KEY_CHECK_PROTECTED = "checkProtected"
        const val KEY_CHECK_PRIVATE = "checkPrivate"
    }

    private val isPublicCheckEnabled = getBoolean(KEY_CHECK_PUBLIC, true)
    private val isInternalCheckEnabled = getBoolean(KEY_CHECK_INTERNAL, true)
    private val isProtectedCheckEnabled = getBoolean(KEY_CHECK_PROTECTED, false)
    private val isPrivateCheckEnabled = getBoolean(KEY_CHECK_PRIVATE, false)

    fun checkVisibility(declaration: KtNamedDeclaration): Boolean {
        val classOrObject = declaration.containingClassOrObject
        return isPublicCheckEnabled && isPublic(declaration, classOrObject) ||
                isInternalCheckEnabled && isInternal(declaration, classOrObject) ||
                isProtectedCheckEnabled && declaration.isProtected() ||
                isPrivateCheckEnabled && isPrivate(declaration, classOrObject)
    }

    private fun isPublic(declaration: KtNamedDeclaration, classOrObject: KtClassOrObject?): Boolean {
        return declaration.isPublic && (classOrObject == null || classOrObject.isPublic)
    }

    private fun isInternal(declaration: KtNamedDeclaration, classOrObject: KtClassOrObject?): Boolean {
        val isDeclarationInternal = declaration.isInternal()
        if (classOrObject == null) {
            return isDeclarationInternal
        }
        val isClassOrObjectInternal = classOrObject.isInternal()
        return isDeclarationInternal && isClassOrObjectInternal ||
                isDeclarationInternal && classOrObject.isPublic ||
                isClassOrObjectInternal && declaration.isPublic
    }

    private fun isPrivate(declaration: KtNamedDeclaration, classOrObject: KtClassOrObject?): Boolean {
        return declaration.isPrivate() || (classOrObject == null || classOrObject.isPrivate())
    }

    private fun getBoolean(key: String, default: Boolean): Boolean {
        return rule.valueOrDefault(key, default)
    }
}
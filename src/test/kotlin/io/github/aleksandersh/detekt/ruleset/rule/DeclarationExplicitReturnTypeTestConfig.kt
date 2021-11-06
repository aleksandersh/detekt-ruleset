package io.github.aleksandersh.detekt.ruleset.rule

import io.gitlab.arturbosch.detekt.api.Config

class DeclarationExplicitReturnTypeTestConfig(
    private val isPublicEnabled: Boolean,
    private val isInternalEnabled: Boolean,
    private val isProtectedEnabled: Boolean,
    private val isPrivateEnabled: Boolean
) : Config {

    override fun subConfig(key: String): Config {
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> valueOrNull(key: String): T? {
        return when (key) {
            DeclarationExplicitReturnType.KEY_CHECK_PUBLIC -> isPublicEnabled as T?
            DeclarationExplicitReturnType.KEY_CHECK_INTERNAL -> isInternalEnabled as T?
            DeclarationExplicitReturnType.KEY_CHECK_PROTECTED -> isProtectedEnabled as T?
            DeclarationExplicitReturnType.KEY_CHECK_PRIVATE -> isPrivateEnabled as T?
            Config.ACTIVE_KEY -> true as T?
            else -> null
        }
    }
}
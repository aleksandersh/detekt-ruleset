package io.github.aleksandersh.detekt.ruleset

import io.github.aleksandersh.detekt.ruleset.rule.*
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.ConfigValidator
import io.gitlab.arturbosch.detekt.api.Notification
import io.gitlab.arturbosch.detekt.api.internal.SimpleNotification

class AleksanderShRuleSetConfigValidator : ConfigValidator {

    override fun validate(config: Config): Collection<Notification> {
        val notifications = mutableListOf<Notification>()
        config.checkConfig(AleksanderShRuleSetProvider.ID, notifications) { subConfig ->
            validateMainConfig(subConfig, notifications)
        }
        return notifications
    }

    private fun validateMainConfig(config: Config, notifications: MutableList<Notification>) {
        config.checkConfig(SpecifyFunctionExplicitReturnType.RULE_ID, notifications) { ruleConfig ->
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PUBLIC, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_INTERNAL, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PROTECTED, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PRIVATE, notifications)
        }
        config.checkConfig(SpecifyPropertyExplicitReturnType.RULE_ID, notifications) { ruleConfig ->
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PUBLIC, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_INTERNAL, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PROTECTED, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PRIVATE, notifications)
        }
        config.checkConfig(AvoidFunctionTupleReturnType.RULE_ID, notifications) { ruleConfig ->
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PUBLIC, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_INTERNAL, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PROTECTED, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PRIVATE, notifications)
        }
        config.checkConfig(AvoidPropertyTupleReturnType.RULE_ID, notifications) { ruleConfig ->
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PUBLIC, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_INTERNAL, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PROTECTED, notifications)
            ruleConfig.checkBooleanProperty(DeclarationVisibilityCheck.KEY_CHECK_PRIVATE, notifications)
        }
    }

    private inline fun Config.checkConfig(
        name: String,
        notifications: MutableList<Notification>,
        checkBlock: (subConfig: Config) -> Unit
    ) {
        runCatching { subConfig(name) }
            .onSuccess { subConfig -> checkBlock(subConfig) }
            .onFailure { notifications.addError("Missing $name config") }
    }

    private fun Config.checkBooleanProperty(name: String, notifications: MutableList<Notification>) {
        runCatching { valueOrNull<Boolean>(name) }
            .onFailure { notifications.addError("Incorrect $name property") }
    }

    private fun MutableList<Notification>.addError(text: String) {
        add(SimpleNotification(text))
    }
}
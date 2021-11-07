package io.github.aleksandersh.detekt.ruleset

import io.github.aleksandersh.detekt.ruleset.rule.AvoidFunctionTupleReturnType
import io.github.aleksandersh.detekt.ruleset.rule.AvoidPropertyTupleReturnType
import io.github.aleksandersh.detekt.ruleset.rule.SpecifyFunctionExplicitReturnType
import io.github.aleksandersh.detekt.ruleset.rule.SpecifyPropertyExplicitReturnType
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class AleksanderShRuleSetProvider : RuleSetProvider {

    companion object {

        const val ID = "AleksanderShRuleSet"
    }

    override val ruleSetId: String = ID

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId, listOf(
                SpecifyFunctionExplicitReturnType(config),
                SpecifyPropertyExplicitReturnType(config),
                AvoidFunctionTupleReturnType(config),
                AvoidPropertyTupleReturnType(config)
            )
        )
    }
}
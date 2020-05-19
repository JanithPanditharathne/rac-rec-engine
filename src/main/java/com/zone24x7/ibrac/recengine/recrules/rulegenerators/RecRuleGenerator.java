package com.zone24x7.ibrac.recengine.recrules.rulegenerators;

import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;

public interface RecRuleGenerator {
    /**
     * Method to generate rec rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @return only recommend rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    String generateRecRule(String ruleId, String matchingCondition) throws RuleGeneratorException;
}

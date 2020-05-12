package com.zone24x7.ibrac.recengine.rules.rulegenerators;

import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;

/**
 * Interface for merchandising rule generator.
 */
public interface MerchandisingRuleGenerator {
    /**
     * Method to generate a boost rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action condition to be included
     * @param limit             limit to boost
     * @return boost rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    String generateBoostRule(String ruleId, String matchingCondition, String actionCondition, Integer limit) throws RuleGeneratorException;

    /**
     * Method to generate a bury rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action condition to be included
     * @return bury rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    String generateBuryRule(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException;

    /**
     * Method to generate a only recommend rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action condition to be included
     * @return only recommend rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    String generateOnlyRecommendRule(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException;

    /**
     * Method to generate a do not recommend rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action condition to be included
     * @return do not recommend rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    String generateDoNotRecommendRule(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException;
}

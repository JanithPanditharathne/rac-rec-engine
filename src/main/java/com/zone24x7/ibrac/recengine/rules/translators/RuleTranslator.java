package com.zone24x7.ibrac.recengine.rules.translators;

import com.zone24x7.ibrac.recengine.rules.exceptions.InvalidRuleException;

import java.util.Map;

/**
 * Interface to translate rules.
 */
public interface RuleTranslator {
    /**
     * Method to convert a rule to a matching condition.
     *
     * @param rule the rule to be converted
     * @return the generated matching condition
     * @throws InvalidRuleException if a rule is invalid
     */
    String convertToMatchingCondition(String rule) throws InvalidRuleException;

    /**
     * Method to convert a rule to a action condition.
     *
     * @param rule     the rule to be converted
     * @param uniqueId the unique id to be added for the condition of the rule
     * @return the generated action condition
     * @throws InvalidRuleException if a rule is invalid
     */
    String convertToActionCondition(String rule, String uniqueId) throws InvalidRuleException;

    /**
     * Method to set the attribute mapping information.
     * Example: if price attribute is a double, there should be a map entry for price|double
     *
     * @param mappingInfo the mapping information
     */
    void setAttributeTypeMappingInfo(Map<String, String> mappingInfo);
}

package com.zone24x7.ibrac.recengine.rules.merchandisingrules.rulegenerators;

import com.zone24x7.ibrac.recengine.enumeration.RuleType;
import com.zone24x7.ibrac.recengine.exceptions.InvalidRuleException;
import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.translators.RuleTranslator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Drools implementation of merchandising rule generator.
 */
@Component
public class DroolsMerchandisingRuleGenerator implements MerchandisingRuleGenerator {
    private static final String SEPARATOR = "\n";
    private static final String ALIGNMENT_SEPARATOR = "    ";
    private static final String PACKAGE = "package mrulespack";

    private static final List<String> IMPORTS_LIST = Arrays.asList("import java.util.List;",
            "import java.util.LinkedList;",
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;",
            "import com.zone24x7.ibrac.recengine.pojo.Product;",
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;",
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;",
            "import com.zone24x7.ibrac.recengine.util.RuleUtils;");

    private static final String WHEN = "when";
    private static final String THEN = "then";
    private static final String END = "end";

    private static final String GET_PRODUCTS_LIST = "$products: List();";

    private static final String GET_RULE_EXECUTION_STATUS = "$ruleExecutionStatus : RuleExecutionStatus()";

    private static final String ACTION_CONDITIONS_START = "$product : ";
    private static final String ACTION_CONDITIONS_END = " from $products";
    private static final String ACTION_CONDITIONS_START_FOR_REC_BASED_RULES = "$newProductList : LinkedList() from collect(";
    private static final String ACTION_CONDITIONS_END_FOR_REC_BASED_RULES = " from $products)";

    private static final List<String> BOOST_RULE_FOOTER = Arrays.asList("$products.remove($product);",
            "$products.add($ruleExecutionStatus.getHitCount(drools.getRule().getName()), $product);");

    private static final List<String> BURY_RULE_FOOTER = Arrays.asList("$products.remove($product);", "$products.add($product);");

    private static final List<String> ONLY_RECOMMEND_RULE_FOOTER = Arrays.asList("$products.clear();", "$products.addAll($newProductList);");

    private static final List<String> DO_NOT_RECOMMEND_RULE_FOOTER = Arrays.asList("$products.removeAll($newProductList);");

    private static final String DO_NOT_RECOMMEND_RULE_FOOTER_CONDITION_START = "if (!$newProductList.isEmpty()) {";
    private static final String DO_NOT_RECOMMEND_RULE_FOOTER_CONDITION_END = "}";

    private static final String EXECUTION_STATUS_THEN_CONDITION_FOR_BOOST = "$ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BOOST);";
    private static final String EXECUTION_STATUS_THEN_CONDITION_FOR_BURY = "$ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BURY);";
    private static final String EXECUTION_STATUS_THEN_CONDITION_FOR_ONLY_REC = "$ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.ONLY_RECOMMEND);";
    private static final String EXECUTION_STATUS_THEN_CONDITION_FOR_DO_NOT_REC = "$ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.DO_NOT_RECOMMEND);";

    private static final String ACTION_CONDITIONS_NULL_OR_EMPTY_ERR_MSG = "Action conditions cannot be null or empty.";
    private static final String RULE_ID_NULL_OR_EMPTY_ERR_MSG = "Rule Id cannot be null or empty.";
    private static final String INVALID_RULE_ERR_MSG = "CS rule to RE rule conversion error occurred.";

    @Autowired
    private RuleTranslator ruleTranslator;

    /**
     * Method to generate a boost rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action rule to be included
     * @param limit             limit to boost
     * @return boost rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    @Override
    public String generateBoostRule(String ruleId, String matchingCondition, String actionCondition, Integer limit) throws RuleGeneratorException {
        TranslatedConditions translatedConditions = translateConditions(ruleId, matchingCondition, actionCondition);

        return generateFinalDroolsRule(ruleId,
                translatedConditions.getTranslatedMatchingCondition(),
                ACTION_CONDITIONS_START + translatedConditions.getTranslatedActionCondition() + ACTION_CONDITIONS_END,
                RuleType.BOOST,
                limit);
    }

    /**
     * Method to generate a bury rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action rule to be included
     * @return bury rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    @Override
    public String generateBuryRule(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException {
        TranslatedConditions translatedConditions = translateConditions(ruleId, matchingCondition, actionCondition);

        return generateFinalDroolsRule(ruleId,
                translatedConditions.getTranslatedMatchingCondition(),
                ACTION_CONDITIONS_START + translatedConditions.getTranslatedActionCondition() + ACTION_CONDITIONS_END,
                RuleType.BURY,
                null);
    }

    /**
     * Method to generate a only recommend rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action rule to be included
     * @return only recommend rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    @Override
    public String generateOnlyRecommendRule(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException {
        TranslatedConditions translatedConditions = translateConditions(ruleId, matchingCondition, actionCondition);

        return generateFinalDroolsRule(ruleId, translatedConditions.getTranslatedMatchingCondition(),
                ACTION_CONDITIONS_START_FOR_REC_BASED_RULES + translatedConditions.getTranslatedActionCondition() + ACTION_CONDITIONS_END_FOR_REC_BASED_RULES,
                RuleType.ONLY_RECOMMEND,
                null);
    }

    /**
     * Method to generate a do not recommend rule for a given matching condition and an action condition.
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @param actionCondition   action rule to be included
     * @return do not recommend rule in drools format
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    @Override
    public String generateDoNotRecommendRule(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException {
        TranslatedConditions translatedConditions = translateConditions(ruleId, matchingCondition, actionCondition);

        return generateFinalDroolsRule(ruleId, translatedConditions.getTranslatedMatchingCondition(),
                ACTION_CONDITIONS_START_FOR_REC_BASED_RULES + translatedConditions.getTranslatedActionCondition() + ACTION_CONDITIONS_END_FOR_REC_BASED_RULES,
                RuleType.DO_NOT_RECOMMEND,
                null);
    }

    /**
     * Method to translate the matching conditions and action condition
     *
     * @param ruleId            the rule id
     * @param matchingCondition the matching condition
     * @param actionCondition   the action condition
     * @return the translated conditions
     * @throws RuleGeneratorException if an error occurs while generating rules
     */
    private TranslatedConditions translateConditions(String ruleId, String matchingCondition, String actionCondition) throws RuleGeneratorException {
        if (StringUtils.isEmpty(ruleId)) {
            throw new RuleGeneratorException(RULE_ID_NULL_OR_EMPTY_ERR_MSG);
        }

        if (StringUtils.isEmpty(actionCondition)) {
            throw new RuleGeneratorException(ACTION_CONDITIONS_NULL_OR_EMPTY_ERR_MSG);
        }

        String translatedMatchingCondition = "";
        String translatedActionsCondition;

        try {
            if (StringUtils.isNotEmpty(matchingCondition)) {
                translatedMatchingCondition = ruleTranslator.convertToMatchingCondition(matchingCondition);
            }

            translatedActionsCondition = ruleTranslator.convertToActionCondition(actionCondition, ruleId);
        } catch (InvalidRuleException e) {
            throw new RuleGeneratorException(INVALID_RULE_ERR_MSG, e);
        }

        return new TranslatedConditions(translatedMatchingCondition, translatedActionsCondition);
    }

    /**
     * Method to generate the final drools rule.
     *
     * @param ruleId                      the rule id
     * @param translatedMatchingCondition the translated matching condition
     * @param translatedActionsCondition  the translated action condition
     * @param ruleType                    the rule type
     * @param limit                       the limit
     * @return the generate drools rule string
     */
    private String generateFinalDroolsRule(String ruleId, String translatedMatchingCondition, String translatedActionsCondition, RuleType ruleType, Integer limit) {
        StringBuilder builder = new StringBuilder();

        builder.append(PACKAGE).append(SEPARATOR).append(SEPARATOR)
                .append(StringUtils.join(IMPORTS_LIST, SEPARATOR)).append(SEPARATOR).append(SEPARATOR)
                .append(generateRuleHeader(ruleId))
                .append(SEPARATOR);

        builder.append(WHEN).append(SEPARATOR);

        if (StringUtils.isNotEmpty(translatedMatchingCondition)) {
            builder.append(ALIGNMENT_SEPARATOR).append(translatedMatchingCondition).append(SEPARATOR);
        }

        builder.append(ALIGNMENT_SEPARATOR).append(GET_PRODUCTS_LIST).append(SEPARATOR);
        builder.append(ALIGNMENT_SEPARATOR).append(translatedActionsCondition).append(SEPARATOR);
        builder.append(ALIGNMENT_SEPARATOR).append(GET_RULE_EXECUTION_STATUS).append(SEPARATOR);

        builder.append(THEN).append(SEPARATOR);

        String limitBasedAlignmentSeparator = ALIGNMENT_SEPARATOR;

        if (limit != null) {
            builder.append(ALIGNMENT_SEPARATOR).append(generateThenStatementForLimit(limit)).append(SEPARATOR);
            limitBasedAlignmentSeparator += ALIGNMENT_SEPARATOR;
        }

        if (ruleType == RuleType.BOOST) {
            builder.append(limitBasedAlignmentSeparator).append(StringUtils.join(BOOST_RULE_FOOTER, SEPARATOR + limitBasedAlignmentSeparator)).append(SEPARATOR);
            builder.append(limitBasedAlignmentSeparator).append(EXECUTION_STATUS_THEN_CONDITION_FOR_BOOST).append(SEPARATOR);
        } else if (ruleType == RuleType.BURY) {
            builder.append(ALIGNMENT_SEPARATOR).append(StringUtils.join(BURY_RULE_FOOTER, SEPARATOR + ALIGNMENT_SEPARATOR)).append(SEPARATOR);
            builder.append(ALIGNMENT_SEPARATOR).append(EXECUTION_STATUS_THEN_CONDITION_FOR_BURY).append(SEPARATOR);
        } else if (ruleType == RuleType.ONLY_RECOMMEND) {
            builder.append(ALIGNMENT_SEPARATOR).append(StringUtils.join(ONLY_RECOMMEND_RULE_FOOTER, SEPARATOR + ALIGNMENT_SEPARATOR)).append(SEPARATOR);
            builder.append(ALIGNMENT_SEPARATOR).append(EXECUTION_STATUS_THEN_CONDITION_FOR_ONLY_REC).append(SEPARATOR);
        } else if (ruleType == RuleType.DO_NOT_RECOMMEND) {
            builder.append(ALIGNMENT_SEPARATOR).append(DO_NOT_RECOMMEND_RULE_FOOTER_CONDITION_START).append(SEPARATOR);
            builder.append(ALIGNMENT_SEPARATOR).append(ALIGNMENT_SEPARATOR).append(StringUtils.join(DO_NOT_RECOMMEND_RULE_FOOTER, SEPARATOR + ALIGNMENT_SEPARATOR + ALIGNMENT_SEPARATOR)).append(SEPARATOR);
            builder.append(ALIGNMENT_SEPARATOR).append(ALIGNMENT_SEPARATOR).append(EXECUTION_STATUS_THEN_CONDITION_FOR_DO_NOT_REC).append(SEPARATOR);
            builder.append(ALIGNMENT_SEPARATOR).append(DO_NOT_RECOMMEND_RULE_FOOTER_CONDITION_END).append(SEPARATOR);
        }

        if (limit != null) {
            builder.append(ALIGNMENT_SEPARATOR).append("}").append(SEPARATOR);
        }

        builder.append(END);

        return builder.toString();
    }

    /**
     * Method to generate rule headers.
     *
     * @param id Rule ID.
     * @return Rule header string.
     */
    private static String generateRuleHeader(String id) {
        return "rule \"" + id + "\"";
    }

    /**
     * Method to generate then statement for limit.
     *
     * @param limit the limit
     * @return the generated then statement for limit
     */
    private static String generateThenStatementForLimit(Integer limit) {
        return "if ($ruleExecutionStatus.getHitCount(drools.getRule().getName()) < " + limit + ") {";
    }

    /**
     * Class to represent translated conditions.
     */
    private static class TranslatedConditions {
        private String translatedMatchingCondition;
        private String translatedActionCondition;

        /**
         * Constructor to instantiate TranslatedConditions.
         *
         * @param translatedMatchingCondition the translated matching condition
         * @param translatedActionCondition   the translated action condition
         */
        TranslatedConditions(String translatedMatchingCondition, String translatedActionCondition) {
            this.translatedMatchingCondition = translatedMatchingCondition;
            this.translatedActionCondition = translatedActionCondition;
        }

        /**
         * Method to get the translated matching condition.
         *
         * @return the translated matching condition
         */
        String getTranslatedMatchingCondition() {
            return translatedMatchingCondition;
        }

        /**
         * Method to get the translated action condition.
         *
         * @return the translated action condition
         */
        String getTranslatedActionCondition() {
            return translatedActionCondition;
        }
    }
}

package com.zone24x7.ibrac.recengine.rules.recrules.rulegenerators;

import com.zone24x7.ibrac.recengine.exceptions.InvalidRuleException;
import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.translators.RuleTranslator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Drools rec Rule generator which constructs a single rules
 */
@Component
public class DroolsRecRuleGenerator implements RecRuleGenerator {
    private static final String SEPARATOR = "\n";
    private static final String ALIGNMENT_SEPARATOR = "    ";
    private static final String PACKAGE = "package recrulespack";

    private static final List<String> IMPORTS_LIST = Arrays.asList("import java.util.List;",
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;",
            "import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;");

    private static final String JAVA_CLASS_INJECTIONS = "$recs: List();\n    RecRuleKnowledgeBaseInfo($recIdToRecMap : recIdToRecMap);";

    private static final String WHEN = "when";
    private static final String THEN = "then";
    private static final String END = "end";

    private static final String INVALID_RULE_ERR_MSG = "CS rule to RE rule conversion error occurred.";
    private static final String RULE_ID_NULL_OR_EMPTY_ERR_MSG = "Empty Rule Id";

    private static final String ADD_TO_RECS_START = "$recs.add($recIdToRecMap.get(\"";
    private static final String ADD_TO_RECS_END = "\"));";

    @Autowired
    private RuleTranslator ruleTranslator;

    /**
     * Generates rec rule based on the ruleId and the matching condition provided
     *
     * @param ruleId            rule id
     * @param matchingCondition matching condition to be included
     * @return the drools rule string
     * @throws RuleGeneratorException if an error occurred while constructing the rule
     */
    @Override
    public String generateRecRule(String ruleId, String matchingCondition) throws RuleGeneratorException {

        if (StringUtils.isEmpty(ruleId)) {
            throw new RuleGeneratorException(RULE_ID_NULL_OR_EMPTY_ERR_MSG);
        }

        try {
            String translatedMatchingCondition = "";
            if (StringUtils.isNotEmpty(matchingCondition)) {
                translatedMatchingCondition = ruleTranslator.convertToMatchingCondition(matchingCondition);
            }
            return generateFinalDroolsRule(ruleId, translatedMatchingCondition);
        } catch (InvalidRuleException e) {
            throw new RuleGeneratorException(INVALID_RULE_ERR_MSG, e);
        }
    }

    /**
     * Method to generate the final drools rule.
     *
     * @param ruleId                      the rule id
     * @param translatedMatchingCondition the translated matching condition
     * @return the generate drools rule string
     */
    private String generateFinalDroolsRule(String ruleId, String translatedMatchingCondition) {
        StringBuilder builder = new StringBuilder();

        builder.append(PACKAGE).append(SEPARATOR).append(SEPARATOR)
                .append(StringUtils.join(IMPORTS_LIST, SEPARATOR)).append(SEPARATOR).append(SEPARATOR)
                .append(generateRuleHeader(ruleId))
                .append(SEPARATOR);

        builder.append(WHEN).append(SEPARATOR);
        builder.append(ALIGNMENT_SEPARATOR).append(JAVA_CLASS_INJECTIONS).append(SEPARATOR);

        if (StringUtils.isNotEmpty(translatedMatchingCondition)) {
            builder.append(ALIGNMENT_SEPARATOR).append(translatedMatchingCondition).append(SEPARATOR);
        }

        builder.append(THEN).append(SEPARATOR);

        builder.append(ALIGNMENT_SEPARATOR).append(ADD_TO_RECS_START).append(ruleId).append(ADD_TO_RECS_END).append(SEPARATOR);

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
}

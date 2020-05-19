package com.zone24x7.ibrac.recengine.rules.recrules.rulegenerators;

import com.zone24x7.ibrac.recengine.exceptions.InvalidRuleException;
import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.translators.CsRuleToDroolsRuleTranslator;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.translators.RuleTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test class for DroolsRecRuleGenerator
 */
class DroolsRecRuleGeneratorTest {

    private DroolsRecRuleGenerator droolsRecRuleGenerator;
    private RuleTranslator ruleTranslator;
    private Map<String, String> attributeMap = new HashMap<>();
    private static final String EXPECTED_AND_CONDITION = "package recrulespack\n\n" +
            "import java.util.List;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;\n" +
            "\nrule \"100\"\n" +
            "when\n" +
            "    $recs: List();\n" +
            "    RecRuleKnowledgeBaseInfo($recIdToRecMap : recIdToRecMap);\n" +
            "    MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"Clothing\" && matchingMap[\"brand\"] equalsIgnoreCase \"Tommy\")\n" +
            "then\n" +
            "    $recs.add($recIdToRecMap.get(\"100\"));\n" +
            "end";

    private static final String EXPECTED_AND_OR_CONDITION = "package recrulespack\n\n" +
            "import java.util.List;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;\n" +
            "\nrule \"100\"\n" +
            "when\n" +
            "    $recs: List();\n" +
            "    RecRuleKnowledgeBaseInfo($recIdToRecMap : recIdToRecMap);\n" +
            "    MatchingCondition((matchingMap[\"department\"] equalsIgnoreCase \"Clothing\" || matchingMap[\"department\"] equalsIgnoreCase \"Shoes\") && matchingMap[\"brand\"] equalsIgnoreCase \"Tommy\")\n" +
            "then\n" +
            "    $recs.add($recIdToRecMap.get(\"100\"));\n" +
            "end";

    @BeforeEach
    void setUp() {
        droolsRecRuleGenerator = new DroolsRecRuleGenerator();
        ruleTranslator = spy(new CsRuleToDroolsRuleTranslator(attributeMap));
        ReflectionTestUtils.setField(droolsRecRuleGenerator, "ruleTranslator", ruleTranslator);
    }

    /**
     * Should throw RuleGeneratorException when empty ruleId
     */
    @Test
    void should_throw_RuleGeneratorException_when_empty_ruleId() throws RuleGeneratorException {
        assertThrows(RuleGeneratorException.class, () -> {
            droolsRecRuleGenerator.generateRecRule("", "department #= \"Clothing\" && brand #= \"Tommy\"");
        });
    }

    /**
     * Should throw RuleGeneratorException if translator throws an exception
     */
    @Test
    void should_throw_RuleGeneratorException_when_translator_throws_an_exception() throws InvalidRuleException {
        when(ruleTranslator.convertToMatchingCondition("department #= \"Clothing\" && brand #= \"Tommy\"")).thenThrow(new InvalidRuleException(""));
        assertThrows(RuleGeneratorException.class, () -> {
            droolsRecRuleGenerator.generateRecRule("100", "department #= \"Clothing\" && brand #= \"Tommy\"");
        });
    }

    /**
     * Should return expected drools format for single and matching condition
     */
    @Test
    void should_return_expected_drools_format_with_single_and_matching_condition() throws RuleGeneratorException {
        String result = droolsRecRuleGenerator.generateRecRule("100", "department #= \"Clothing\" && brand #= \"Tommy\"");
        assertThat(result, equalTo(EXPECTED_AND_CONDITION));
    }

    /**
     * Should return expected drools format for and+or matching condition
     */
    @Test
    void should_return_expected_drools_format_with_and_or_matching_condition() throws RuleGeneratorException {
        String result = droolsRecRuleGenerator.generateRecRule("100", "(department #= \"Clothing\" || department #= \"Shoes\") && brand #= \"Tommy\"");
        assertThat(result, equalTo(EXPECTED_AND_OR_CONDITION));
    }
}
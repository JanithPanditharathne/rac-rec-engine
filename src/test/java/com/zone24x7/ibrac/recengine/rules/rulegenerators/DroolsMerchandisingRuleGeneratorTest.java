package com.zone24x7.ibrac.recengine.rules.rulegenerators;

import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.rules.translators.CsRuleToDroolsRuleTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class to test DroolsMerchandisingRuleGenerator.
 */
public class DroolsMerchandisingRuleGeneratorTest {
    private DroolsMerchandisingRuleGenerator ruleGenerator;

    private static final String EXPECTED_RULE_1 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"1\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $product : Product((\"1\" == \"1\") && (attributesMap[\"department\"] equalsIgnoreCase \"SHOES\")) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.remove($product);\n" +
            "    $products.add($ruleExecutionStatus.getHitCount(drools.getRule().getName()), $product);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BOOST);\n" +
            "end";

    private static final String EXPECTED_RULE_2 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"2\"\n" +
            "when\n" +
            "    MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"SHOES\" && matchingMap[\"category\"] equalsIgnoreCase \"Sports\")\n" +
            "    $products: List();\n" +
            "    $product : Product((\"2\" == \"2\") && ((attributesMap[\"department\"] equalsIgnoreCase \"SHOES\") || (attributesMap[\"brand\"] equalsIgnoreCase \"Nike\" && attributesMap[\"category\"] equalsIgnoreCase \"Tops\"))) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.remove($product);\n" +
            "    $products.add($ruleExecutionStatus.getHitCount(drools.getRule().getName()), $product);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BOOST);\n" +
            "end";

    private static final String EXPECTED_RULE_3 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"3\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $product : Product((\"3\" == \"3\") && (attributesMap[\"department\"] equalsIgnoreCase \"SHOES\")) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    if ($ruleExecutionStatus.getHitCount(drools.getRule().getName()) < 2) {\n" +
            "        $products.remove($product);\n" +
            "        $products.add($ruleExecutionStatus.getHitCount(drools.getRule().getName()), $product);\n" +
            "        $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BOOST);\n" +
            "    }\n" +
            "end";

    private static final String EXPECTED_RULE_4 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"4\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $product : Product((\"4\" == \"4\") && (attributesMap[\"department\"] equalsIgnoreCase \"wrong department\")) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.remove($product);\n" +
            "    $products.add($ruleExecutionStatus.getHitCount(drools.getRule().getName()), $product);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BOOST);\n" +
            "end";

    private static final String EXPECTED_RULE_5 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"5\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $product : Product((\"5\" == \"5\") && (attributesMap[\"department\"] equalsIgnoreCase \"SHOES\")) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.remove($product);\n" +
            "    $products.add($product);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BURY);\n" +
            "end";

    private static final String EXPECTED_RULE_6 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"6\"\n" +
            "when\n" +
            "    MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"SHOES\" && matchingMap[\"category\"] equalsIgnoreCase \"Sports\")\n" +
            "    $products: List();\n" +
            "    $product : Product((\"6\" == \"6\") && ((attributesMap[\"department\"] equalsIgnoreCase \"SHOES\") || (attributesMap[\"brand\"] equalsIgnoreCase \"Nike\" && attributesMap[\"category\"] equalsIgnoreCase \"Tops\"))) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.remove($product);\n" +
            "    $products.add($product);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BURY);\n" +
            "end";

    private static final String EXPECTED_RULE_7 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"7\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $product : Product((\"7\" == \"7\") && (attributesMap[\"department\"] equalsIgnoreCase \"wrong department\")) from $products\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.remove($product);\n" +
            "    $products.add($product);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.BURY);\n" +
            "end";

    private static final String EXPECTED_RULE_8 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"8\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $newProductList : LinkedList() from collect(Product((\"8\" == \"8\") && (attributesMap[\"department\"] equalsIgnoreCase \"SHOES\")) from $products)\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.clear();\n" +
            "    $products.addAll($newProductList);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.ONLY_RECOMMEND);\n" +
            "end";

    private static final String EXPECTED_RULE_9 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"9\"\n" +
            "when\n" +
            "    MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"SHOES\" && matchingMap[\"category\"] equalsIgnoreCase \"Sports\")\n" +
            "    $products: List();\n" +
            "    $newProductList : LinkedList() from collect(Product((\"9\" == \"9\") && ((attributesMap[\"department\"] equalsIgnoreCase \"SHOES\") || (attributesMap[\"brand\"] equalsIgnoreCase \"Nike\" && attributesMap[\"category\"] equalsIgnoreCase \"Tops\"))) from $products)\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.clear();\n" +
            "    $products.addAll($newProductList);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.ONLY_RECOMMEND);\n" +
            "end";

    private static final String EXPECTED_RULE_10 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"10\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $newProductList : LinkedList() from collect(Product((\"10\" == \"10\") && (attributesMap[\"department\"] equalsIgnoreCase \"wrong department\")) from $products)\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    $products.clear();\n" +
            "    $products.addAll($newProductList);\n" +
            "    $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.ONLY_RECOMMEND);\n" +
            "end";

    private static final String EXPECTED_RULE_11 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"11\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $newProductList : LinkedList() from collect(Product((\"11\" == \"11\") && (attributesMap[\"department\"] equalsIgnoreCase \"SHOES\")) from $products)\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    if (!$newProductList.isEmpty()) {\n" +
            "        $products.removeAll($newProductList);\n" +
            "        $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.DO_NOT_RECOMMEND);\n" +
            "    }\n" +
            "end";

    private static final String EXPECTED_RULE_12 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"12\"\n" +
            "when\n" +
            "    MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"SHOES\" && matchingMap[\"category\"] equalsIgnoreCase \"Sports\")\n" +
            "    $products: List();\n" +
            "    $newProductList : LinkedList() from collect(Product((\"12\" == \"12\") && ((attributesMap[\"department\"] equalsIgnoreCase \"SHOES\") || (attributesMap[\"brand\"] equalsIgnoreCase \"Nike\" && attributesMap[\"category\"] equalsIgnoreCase \"Tops\"))) from $products)\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    if (!$newProductList.isEmpty()) {\n" +
            "        $products.removeAll($newProductList);\n" +
            "        $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.DO_NOT_RECOMMEND);\n" +
            "    }\n" +
            "end";

    private static final String EXPECTED_RULE_13 = "package mrulespack\n" +
            "\n" +
            "import java.util.List;\n" +
            "import java.util.LinkedList;\n" +
            "import com.zone24x7.ibrac.recengine.enumeration.RuleType;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.Product;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.RuleExecutionStatus;\n" +
            "\n" +
            "rule \"13\"\n" +
            "when\n" +
            "    $products: List();\n" +
            "    $newProductList : LinkedList() from collect(Product((\"13\" == \"13\") && (attributesMap[\"department\"] equalsIgnoreCase \"wrong department\")) from $products)\n" +
            "    $ruleExecutionStatus : RuleExecutionStatus()\n" +
            "then\n" +
            "    if (!$newProductList.isEmpty()) {\n" +
            "        $products.removeAll($newProductList);\n" +
            "        $ruleExecutionStatus.addExecutedRuleId(drools.getRule().getName(), RuleType.DO_NOT_RECOMMEND);\n" +
            "    }\n" +
            "end";

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        ruleGenerator = new DroolsMerchandisingRuleGenerator();
        ReflectionTestUtils.setField(ruleGenerator, "ruleTranslator", new CsRuleToDroolsRuleTranslator(new HashMap<>()));
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is empty in boost rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_empty_in_boost_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBoostRule("", "department #= \"SHOES\"", "department #= \"SHOES\"", null));
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is null in boost rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_null_in_boost_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBoostRule(null, "department #= \"SHOES\"", "department #= \"SHOES\"", null));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is empty in boost rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_empty_in_boost_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBoostRule("1", "department #= \"SHOES\"", "", null));
    }

    /**
     * Test to verify that rule generator exception is thrown when matching condition is malformed in boost rules.
     */
    @Test
    public void should_throw_an_exception_if_matching_condition_is_malformed_in_boost_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBoostRule("1", "department #=", "department #= \"SHOES\"", null));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is malformed in boost rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_malformed_in_boost_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBoostRule("1", "department #= \"Shoes\"", "department #=", null));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is null in boost rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_null_in_boost_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBoostRule("1", "department #= \"SHOES\"", null, null));
    }

    /**
     * Test to verify that boost rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_boost_rule_correctly_when_matching_condition_is_null() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBoostRule("1", null, "department #= \"SHOES\"", null);
        assertEquals(EXPECTED_RULE_1, actualResult);
    }

    /**
     * Test to verify that boost rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_boost_rule_correctly_when_matching_condition_is_empty() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBoostRule("1", "", "department #= \"SHOES\"", null);
        assertEquals(EXPECTED_RULE_1, actualResult);
    }

    /**
     * Test to verify that boost rule is generated correctly when the matching condition is there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_boost_rule_correctly_when_matching_condition_is_there() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBoostRule("2", "department #= \"SHOES\" && category #= \"Sports\"", "(department #= \"SHOES\") || (brand #= \"Nike\" && category #= \"Tops\")", null);
        assertEquals(EXPECTED_RULE_2, actualResult);
    }

    /**
     * Test to verify that boost rule is generated correctly when the matching condition is there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_boost_rule_correctly_when_action_condition_has_a_limit() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBoostRule("3", null, "department #= \"SHOES\"", 2);
        assertEquals(EXPECTED_RULE_3, actualResult);
    }

    /**
     * Test to verify that boost rule is generated correctly.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_boost_rule_correctly2() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBoostRule("4", null, "department #= \"wrong department\"", null);
        assertEquals(EXPECTED_RULE_4, actualResult);
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is empty in bury rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_empty_in_bury_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBuryRule("", "department #= \"SHOES\"", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is null in bury rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_null_in_bury_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBuryRule(null, "department #= \"SHOES\"", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is empty in bury rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_empty_in_bury_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBuryRule("5", "department #= \"SHOES\"", ""));
    }

    /**
     * Test to verify that rule generator exception is thrown when matching condition is malformed in bury rules.
     */
    @Test
    public void should_throw_an_exception_if_matching_condition_is_malformed_in_bury_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBuryRule("5", "department #=", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is malformed in bury rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_malformed_in_bury_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBuryRule("5", "department #= \"Shoes\"", "department #="));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is null in bury rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_null_in_bury_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateBuryRule("5", "department #= \"SHOES\"", null));
    }

    /**
     * Test to verify that bury rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_bury_rule_correctly_when_matching_condition_is_null() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBuryRule("5", null, "department #= \"SHOES\"");
        assertEquals(EXPECTED_RULE_5, actualResult);
    }

    /**
     * Test to verify that bury rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_bury_rule_correctly_when_matching_condition_is_empty() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBuryRule("5", "", "department #= \"SHOES\"");
        assertEquals(EXPECTED_RULE_5, actualResult);
    }

    /**
     * Test to verify that bury rule is generated correctly when the matching condition is there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_bury_rule_correctly_when_matching_condition_is_there() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBuryRule("6", "department #= \"SHOES\" && category #= \"Sports\"", "(department #= \"SHOES\") || (brand #= \"Nike\" && category #= \"Tops\")");
        assertEquals(EXPECTED_RULE_6, actualResult);
    }

    /**
     * Test to verify that bury rule is generated correctly.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_bury_rule_correctly() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateBuryRule("7", null, "department #= \"wrong department\"");
        assertEquals(EXPECTED_RULE_7, actualResult);
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is empty in only recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_empty_in_only_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateOnlyRecommendRule("", "department #= \"SHOES\"", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is null in only recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_null_in_only_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateOnlyRecommendRule(null, "department #= \"SHOES\"", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is empty in only recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_empty_in_only_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateOnlyRecommendRule("8", "department #= \"SHOES\"", ""));
    }

    /**
     * Test to verify that rule generator exception is thrown when matching condition is malformed in only recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_matching_condition_is_malformed_in_only_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateOnlyRecommendRule("8", "department #=", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is malformed in only recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_malformed_in_only_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateOnlyRecommendRule("8", "department #= \"Shoes\"", "department #="));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is null in only recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_null_in_only_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateOnlyRecommendRule("8", "department #= \"SHOES\"", null));
    }

    /**
     * Test to verify that only recommend rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_only_recommend_rule_correctly_when_matching_condition_is_null() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateOnlyRecommendRule("8", null, "department #= \"SHOES\"");
        assertEquals(EXPECTED_RULE_8, actualResult);
    }

    /**
     * Test to verify that only recommend rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_only_recommend_rule_correctly_when_matching_condition_is_empty() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateOnlyRecommendRule("8", "", "department #= \"SHOES\"");
        assertEquals(EXPECTED_RULE_8, actualResult);
    }

    /**
     * Test to verify that only recommend rule is generated correctly when the matching condition is there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_only_recommend_rule_correctly_when_matching_condition_is_there() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateOnlyRecommendRule("9", "department #= \"SHOES\" && category #= \"Sports\"", "(department #= \"SHOES\") || (brand #= \"Nike\" && category #= \"Tops\")");
        assertEquals(EXPECTED_RULE_9, actualResult);
    }

    /**
     * Test to verify that only recommend rule is generated correctly.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_only_recommend_rule_correctly() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateOnlyRecommendRule("10", null, "department #= \"wrong department\"");
        assertEquals(EXPECTED_RULE_10, actualResult);
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is empty in do not recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_empty_in_do_not_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateDoNotRecommendRule("", "department #= \"SHOES\"", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when rule id is null in do not recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_rule_id_is_null_in_do_not_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateDoNotRecommendRule(null, "department #= \"SHOES\"", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is empty in do not recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_empty_in_do_not_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateDoNotRecommendRule("11", "department #= \"SHOES\"", ""));
    }

    /**
     * Test to verify that rule generator exception is thrown when matching condition is malformed in do not recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_matching_condition_is_malformed_in_do_not_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateDoNotRecommendRule("11", "department #=", "department #= \"SHOES\""));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is malformed in do not recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_malformed_in_do_not_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateDoNotRecommendRule("11", "department #= \"Shoes\"", "department #="));
    }

    /**
     * Test to verify that rule generator exception is thrown when action condition is null in do not recommend rules.
     */
    @Test
    public void should_throw_an_exception_if_action_condition_is_null_in_do_not_recommend_rules() {
        assertThrows(RuleGeneratorException.class, () -> ruleGenerator.generateDoNotRecommendRule("11", "department #= \"SHOES\"", null));
    }

    /**
     * Test to verify that do not recommend rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_do_not_recommend_rule_correctly_when_matching_condition_is_null() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateDoNotRecommendRule("11", null, "department #= \"SHOES\"");
        assertEquals(EXPECTED_RULE_11, actualResult);
    }

    /**
     * Test to verify that do not recommend rule is generated correctly when the matching condition is not there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_do_not_recommend_rule_correctly_when_matching_condition_is_empty() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateDoNotRecommendRule("11", "", "department #= \"SHOES\"");
        assertEquals(EXPECTED_RULE_11, actualResult);
    }

    /**
     * Test to verify that do not recommend rule is generated correctly when the matching condition is there.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_do_not_recommend_rule_correctly_when_matching_condition_is_there() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateDoNotRecommendRule("12", "department #= \"SHOES\" && category #= \"Sports\"", "(department #= \"SHOES\") || (brand #= \"Nike\" && category #= \"Tops\")");
        assertEquals(EXPECTED_RULE_12, actualResult);
    }

    /**
     * Test to verify that do not recommend rule is generated correctly.
     *
     * @throws RuleGeneratorException if an error occurs
     */
    @Test
    public void should_generate_a_do_not_recommend_rule_correctly() throws RuleGeneratorException {
        String actualResult = ruleGenerator.generateDoNotRecommendRule("13", null, "department #= \"wrong department\"");
        assertEquals(EXPECTED_RULE_13, actualResult);
    }
}
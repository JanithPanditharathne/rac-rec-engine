package com.zone24x7.ibrac.recengine.rules.merchandisingrules.translators;

import com.zone24x7.ibrac.recengine.exceptions.InvalidRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to test CsRuleToDroolsRuleTranslator
 */
public class CsRuleToDroolsRuleTranslatorTest {
    private CsRuleToDroolsRuleTranslator csRuleToDroolsRuleTranslator;

    private static final String UUID = "112233";
    private static final String UUID_CONDITION = "(\"112233\" == \"112233\") && ";

    private static final String CS_MATCHING_RULE_1 = "department == \"Clothing\"";
    private static final String CS_MATCHING_RULE_2 = "department #= \"Clothing & Shoes & a & b\"";
    private static final String CS_MATCHING_RULE_3 = "((department #= \"Clothing\" || department == \"Shoes\") && (category == \"sports\" || category #= \"sport\")) && brand #= \"Nike\"";

    private static final String EXPECTED_MATCHING_RULE_1 = "MatchingCondition(matchingMap[\"department\"] == \"Clothing\")";
    private static final String EXPECTED_MATCHING_RULE_2 = "MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"Clothing & Shoes & a & b\")";
    private static final String EXPECTED_MATCHING_RULE_3 = "MatchingCondition(((matchingMap[\"department\"] equalsIgnoreCase \"Clothing\" || matchingMap[\"department\"] == \"Shoes\") && (matchingMap[\"category\"] == \"sports\" || matchingMap[\"category\"] equalsIgnoreCase \"sport\")) && matchingMap[\"brand\"] equalsIgnoreCase \"Nike\")";

    private static final String CS_ACTION_RULE_1 = "department == \"Clothing\"";
    private static final String CS_ACTION_RULE_2 = "department #= \"Clothing & Shoes & a & b\"";
    private static final String CS_ACTION_RULE_3 = "((department #= \"Clothing\" || department == \"Shoes\") && (category == \"sports\" || category #= \"sport\")) && brand #= \"Nike\"";
    private static final String CS_ACTION_RULE_4 = "((department #= \"Clothing\" || regularPrice > \"12.99\") && reviewCount > \"2\")";

    private static final String EXPECTED_ACTION_RULE_1 = "Product(" + UUID_CONDITION + "(attributesMap[\"department\"] == \"Clothing\"))";
    private static final String EXPECTED_ACTION_RULE_1_1 = "Product((attributesMap[\"department\"] == \"Clothing\"))";
    private static final String EXPECTED_ACTION_RULE_2 = "Product(" + UUID_CONDITION + "(attributesMap[\"department\"] equalsIgnoreCase \"Clothing & Shoes & a & b\"))";
    private static final String EXPECTED_ACTION_RULE_3 = "Product(" + UUID_CONDITION + "(((attributesMap[\"department\"] equalsIgnoreCase \"Clothing\" || attributesMap[\"department\"] == \"Shoes\") && (attributesMap[\"category\"] == \"sports\" || attributesMap[\"category\"] equalsIgnoreCase \"sport\")) && attributesMap[\"brand\"] equalsIgnoreCase \"Nike\"))";
    private static final String EXPECTED_ACTION_RULE_4 = "Product(" + UUID_CONDITION + "(((attributesMap[\"department\"] equalsIgnoreCase \"Clothing\" || RuleUtils.toDouble(attributesMap[\"regularPrice\"]) > \"12.99\") && RuleUtils.toInteger(attributesMap[\"reviewCount\"]) > \"2\")))";

    /**
     * Method to setup the dependencies for the test class
     */
    @BeforeEach
    public void setup() {
        Map<String, String> map = new HashMap<>();
        map.put("regularPrice", "double");
        map.put("reviewCount", "integer");
        csRuleToDroolsRuleTranslator = new CsRuleToDroolsRuleTranslator(map);
    }

    /**
     * Test to verify that matching condition is generated correctly for a simple condition.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_matching_condition_correctly_for_single_condition() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToMatchingCondition(CS_MATCHING_RULE_1);
        assertEquals(EXPECTED_MATCHING_RULE_1, actualResult);

        String actualResult2 = csRuleToDroolsRuleTranslator.convertToMatchingCondition(CS_MATCHING_RULE_2);
        assertEquals(EXPECTED_MATCHING_RULE_2, actualResult2);
    }

    /**
     * Test to verify that matching condition is generated correctly for a complex conditions.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_matching_condition_correctly_for_complex_conditions() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToMatchingCondition(CS_MATCHING_RULE_3);
        assertEquals(EXPECTED_MATCHING_RULE_3, actualResult);
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is null.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_null() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition(null));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is empty.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_empty() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition(""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed1() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department = \"shoes\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed2() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department # \"shoes\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed3() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" & category = \"sports\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed4() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" | category = \"sports\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed5() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department =="));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed6() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == "));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed7() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("== \"shoes\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed8() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition(" == \"shoes\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed9() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" &&"));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed10() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" && "));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed11() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" ||"));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed12() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" || "));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed13() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" || category =="));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_matching_rule_is_malformed14() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" category  == \"sports\""));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the action rule is malformed.
     */
    @Test
    public void should_throw_exception_when_the_action_rule_is_malformed() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToActionCondition("== \"shoes\"", "11"));
    }

    /**
     * Test to verify that action condition is generated correctly for a simple condition.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_for_single_condition() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_1, UUID);
        assertEquals(EXPECTED_ACTION_RULE_1, actualResult);

        String actualResult2 = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_2, UUID);
        assertEquals(EXPECTED_ACTION_RULE_2, actualResult2);
    }

    /**
     * Test to verify that action condition is generated correctly when unique id is null.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_when_unique_id_is_null() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_1, null);
        assertEquals(EXPECTED_ACTION_RULE_1_1, actualResult);
    }

    /**
     * Test to verify that action condition is generated correctly when unique id is empty.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_when_unique_id_is_empty() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_1, "");
        assertEquals(EXPECTED_ACTION_RULE_1_1, actualResult);
    }

    /**
     * Test to verify that action condition is generated correctly for a complex conditions.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_for_complex_conditions() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_3, UUID);
        assertEquals(EXPECTED_ACTION_RULE_3, actualResult);
    }

    /**
     * Test to verify that action condition is generated correctly for a complex conditions with double and integer attributes.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_for_complex_conditions_with_double_and_integer_attributes() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_4, UUID);
        assertEquals(EXPECTED_ACTION_RULE_4, actualResult);
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the action rule is null.
     */
    @Test
    public void should_throw_exception_when_the_action_rule_is_null() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToActionCondition(null, UUID));
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the action rule is empty.
     */
    @Test
    public void should_throw_exception_when_the_action_rule_is_empty() {
        assertThrows(InvalidRuleException.class, () -> csRuleToDroolsRuleTranslator.convertToActionCondition("", UUID));
    }
}
package com.zone24x7.ibrac.recengine.rules.translators;

import com.zone24x7.ibrac.recengine.rules.exceptions.InvalidRuleException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
    private static final String CS_ACTION_RULE_4 = "((department #= \"Clothing\" || price > \"12.99\") && rating > \"2\")";

    private static final String EXPECTED_ACTION_RULE_1 = "Product(" + UUID_CONDITION + "(attributesMap[\"department\"] == \"Clothing\"))";
    private static final String EXPECTED_ACTION_RULE_1_1 = "Product((attributesMap[\"department\"] == \"Clothing\"))";
    private static final String EXPECTED_ACTION_RULE_2 = "Product(" + UUID_CONDITION + "(attributesMap[\"department\"] equalsIgnoreCase \"Clothing & Shoes & a & b\"))";
    private static final String EXPECTED_ACTION_RULE_3 = "Product(" + UUID_CONDITION + "(((attributesMap[\"department\"] equalsIgnoreCase \"Clothing\" || attributesMap[\"department\"] == \"Shoes\") && (attributesMap[\"category\"] == \"sports\" || attributesMap[\"category\"] equalsIgnoreCase \"sport\")) && attributesMap[\"brand\"] equalsIgnoreCase \"Nike\"))";
    private static final String EXPECTED_ACTION_RULE_4 = "Product(" + UUID_CONDITION + "(((attributesMap[\"department\"] equalsIgnoreCase \"Clothing\" || Double.valueOf(attributesMap[\"price\"]) > \"12.99\") && Integer.valueOf(attributesMap[\"rating\"]) > \"2\")))";

    /**
     * Method to setup the dependencies for the test class
     */
    @Before
    public void setup() {
        csRuleToDroolsRuleTranslator = new CsRuleToDroolsRuleTranslator();
        Map<String, String> map = new HashMap<>();
        map.put("price", "double");
        map.put("rating", "integer");
        csRuleToDroolsRuleTranslator.setAttributeTypeMappingInfo(map);
    }

    /**
     * Test to verify that matching condition is generated correctly for a simple condition.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_matching_condition_correctly_for_single_condition() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToMatchingCondition(CS_MATCHING_RULE_1);
        Assert.assertEquals(EXPECTED_MATCHING_RULE_1, actualResult);

        String actualResult2 = csRuleToDroolsRuleTranslator.convertToMatchingCondition(CS_MATCHING_RULE_2);
        Assert.assertEquals(EXPECTED_MATCHING_RULE_2, actualResult2);
    }

    /**
     * Test to verify that matching condition is generated correctly for a complex conditions.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_matching_condition_correctly_for_complex_conditions() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToMatchingCondition(CS_MATCHING_RULE_3);
        Assert.assertEquals(EXPECTED_MATCHING_RULE_3, actualResult);
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is null.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_null() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition(null);
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is empty.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_empty() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed1() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department = \"shoes\"");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed2() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department # \"shoes\"");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed3() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" & category = \"sports\"");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed4() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" | category = \"sports\"");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed5() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department ==");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed6() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == ");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed7() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("== \"shoes\"");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test (expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed8() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition(" == \"shoes\"");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed9() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" &&");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed10() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" && ");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed11() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" ||");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed12() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" || ");
    }


    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test (expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed13() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" || category ==");
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the matching rule is malformed.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test (expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_matching_rule_is_malformed14() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToMatchingCondition("department == \"shoes\" category  == \"sports\"");
    }

    /**
     * Test to verify that action condition is generated correctly for a simple condition.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_for_single_condition() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_1, UUID);
        Assert.assertEquals(EXPECTED_ACTION_RULE_1, actualResult);

        String actualResult2 = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_2, UUID);
        Assert.assertEquals(EXPECTED_ACTION_RULE_2, actualResult2);
    }

    /**
     * Test to verify that action condition is generated correctly when unique id is null.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_when_unique_id_is_null() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_1, null);
        Assert.assertEquals(EXPECTED_ACTION_RULE_1_1, actualResult);
    }

    /**
     * Test to verify that action condition is generated correctly when unique id is empty.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_when_unique_id_is_empty() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_1, "");
        Assert.assertEquals(EXPECTED_ACTION_RULE_1_1, actualResult);
    }

    /**
     * Test to verify that action condition is generated correctly for a complex conditions.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_for_complex_conditions() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_3, UUID);
        Assert.assertEquals(EXPECTED_ACTION_RULE_3, actualResult);
    }

    /**
     * Test to verify that action condition is generated correctly for a complex conditions with double and integer attributes.
     *
     * @throws InvalidRuleException if the rule is invalid format
     */
    @Test
    public void should_generate_the_action_condition_correctly_for_complex_conditions_with_double_and_integer_attributes() throws InvalidRuleException {
        String actualResult = csRuleToDroolsRuleTranslator.convertToActionCondition(CS_ACTION_RULE_4, UUID);
        Assert.assertEquals(EXPECTED_ACTION_RULE_4, actualResult);
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the action rule is null.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_action_rule_is_null() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToActionCondition(null, UUID);
    }

    /**
     * Test to verify that InvalidRuleException is thrown when the action rule is empty.
     *
     * @throws InvalidRuleException if the input rec slot object is invalid
     */
    @Test(expected = InvalidRuleException.class)
    public void should_throw_exception_when_the_action_rule_is_empty() throws InvalidRuleException {
        csRuleToDroolsRuleTranslator.convertToActionCondition("", UUID);
    }
}
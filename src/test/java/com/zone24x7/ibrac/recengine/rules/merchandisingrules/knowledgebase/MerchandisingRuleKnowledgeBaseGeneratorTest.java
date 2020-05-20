package com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.rulegenerators.DroolsMerchandisingRuleGenerator;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.rulegenerators.MerchandisingRuleGenerator;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.translators.CsRuleToDroolsRuleTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Class to test MerchandisingRuleKnowledgeBaseGenerator.
 */
public class MerchandisingRuleKnowledgeBaseGeneratorTest {
    private MerchandisingRuleKnowledgeBaseGenerator knowledgeBaseGenerator;

    private static final String RULE_CONFIG_WITH_ZERO_RULES = "{\"rules\":[]}";
    private static final String MALFORMED_JSON_RULE_CONFIG = "\"id\":\"123\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_CONDITION_ERROR = "{\"rules\":[{\"id\":\"111\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department ==\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_INVALID_OPERATOR = "{\"rules\":[{\"id\":\"111\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department abc \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_RULE_ID_NULL = "{\"rules\":[{\"id\":null,\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_RULE_ID_EMPTY = "{\"rules\":[{\"id\":\"\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_TYPE_NULL = "{\"rules\":[{\"id\":\"123\",\"name\":\"Test Rule 1\",\"type\":null,\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_TYPE_EMPTY = "{\"rules\":[{\"id\":\"123\",\"name\":\"Test Rule 1\",\"type\":\"\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_INVALID_TYPE = "{\"rules\":[{\"id\":\"123\",\"name\":\"Test Rule 1\",\"type\":\"INVALID_TYPE\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_ACTION_CONDITION_NULL = "{\"rules\":[{\"id\":\"123\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":null}]}";
    private static final String MALFORMED_JSON_RULE_CONFIG_WITH_ACTION_CONDITION_EMPTY = "{\"rules\":[{\"id\":\"123\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"\"}]}";

    private static final String VALID_RULE_CONFIG_WITH_ONE_RULE = "{\"rules\":[{\"id\":\"111\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";
    private static final String VALID_RULE_CONFIG_WITH_MULTIPLE_RULES = "{\"rules\":[{\"id\":\"111\",\"name\":\"Test Rule 1\",\"type\":\"BOOST\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"},{\"id\":\"222\",\"name\":\"Test Rule 2\",\"type\":\"BURY\",\"isGlobal\":true,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && brand == \\\"Nike\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"},{\"id\":\"333\",\"name\":\"Test Rule 3\",\"type\":\"ONLY_RECOMMEND\",\"isGlobal\":false,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department #= \\\"Clothing\\\" && brand == \\\"Tommy\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"},{\"id\":\"444\",\"name\":\"Test Rule 4\",\"type\":\"DO_NOT_RECOMMEND\",\"isGlobal\":true,\"matchingCondition\":\"(department == \\\"Shoes\\\" || (department == \\\"Clothing\\\" && category == \\\"Tops\\\"))\",\"actionCondition\":\"(brand == \\\"Nike\\\")\"}]}";

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        MerchandisingRuleGenerator ruleGenerator = new DroolsMerchandisingRuleGenerator();
        ReflectionTestUtils.setField(ruleGenerator, "ruleTranslator", new CsRuleToDroolsRuleTranslator(new HashMap<>()));

        knowledgeBaseGenerator = new MerchandisingRuleKnowledgeBaseGenerator();
        ReflectionTestUtils.setField(knowledgeBaseGenerator, "ruleGenerator", ruleGenerator);
        ReflectionTestUtils.setField(knowledgeBaseGenerator, "logger", mock(Logger.class));
    }

    /**
     * Test to verify that knowledge base info is not created when rule config is empty.
     */
    @Test
    public void should_throw_exception_when_rule_config_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(""));
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(null));
    }

    /**
     * Test to verify that knowledge base info with empty knowledge base is generated when there are zero rules.
     *
     * @throws MalformedConfigurationException if an error occurs in rule generation
     */
    @Test
    public void should_create_an_empty_knowledge_base_if_rule_config_has_zero_rules() throws MalformedConfigurationException {
        knowledgeBaseGenerator.setConfigurations(RULE_CONFIG_WITH_ZERO_RULES);
        assertNotNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
        assertTrue(knowledgeBaseGenerator.getKnowledgeBaseInfo().getGlobalFilteringRuleIds().isEmpty());
        assertNotNull(knowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase());
        assertTrue(knowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase().getPackagesMap().isEmpty());
    }

    /**
     * Test to verify that an exception is thrown when the rule config is malformed.
     */
    @Test
    public void should_throw_exception_when_rule_config_is_malformed() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has an error in conditions.
     */
    @Test
    public void should_throw_exception_when_rule_config_is_has_a_error_in_conditions() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_CONDITION_ERROR));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has an invalid operator.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_invalid_operator() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_INVALID_OPERATOR));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has a null rule id.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_null_rule_id() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_RULE_ID_NULL));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has a empty rule id.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_empty_rule_id() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_RULE_ID_EMPTY));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has a null rule type.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_null_type() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_TYPE_NULL));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has a empty rule type.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_empty_type() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_TYPE_EMPTY));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has an invalid rule type.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_invalid_type() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_INVALID_TYPE));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has a null action condition.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_null_action_condition() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_ACTION_CONDITION_NULL));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the rule config has a empty action condition.
     */
    @Test
    public void should_throw_exception_when_rule_config_has_empty_action_condition() {
        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(MALFORMED_JSON_RULE_CONFIG_WITH_ACTION_CONDITION_EMPTY));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the validator throws a validation exception.
     */
    @Test
    public void should_throw_exception_when_validator_throws_validation_exception() {
        Validator validator = mock(Validator.class);
        when(validator.validate(any())).thenThrow(ValidationException.class);
        ReflectionTestUtils.setField(knowledgeBaseGenerator, "validator", validator);

        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(VALID_RULE_CONFIG_WITH_ONE_RULE));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that an exception is thrown when the validator throws a illegal argument exception.
     */
    @Test
    public void should_throw_exception_when_validator_throws_illegal_arg_exception() {
        Validator validator = mock(Validator.class);
        when(validator.validate(any())).thenThrow(IllegalArgumentException.class);
        ReflectionTestUtils.setField(knowledgeBaseGenerator, "validator", validator);

        assertThrows(MalformedConfigurationException.class, () -> knowledgeBaseGenerator.setConfigurations(VALID_RULE_CONFIG_WITH_ONE_RULE));
        assertNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
    }

    /**
     * Test to verify that knowledge base is generated correctly for a single rule config.
     *
     * @throws MalformedConfigurationException if an error occurs in rule generation
     */
    @Test
    public void should_generate_knowledge_base_correctly_for_a_single_rule() throws MalformedConfigurationException {
        knowledgeBaseGenerator.setConfigurations(VALID_RULE_CONFIG_WITH_ONE_RULE);
        assertNotNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
        assertNotNull(knowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase());
        assertEquals(1, knowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase().getPackage("mrulespack").getRules().size());
        assertEquals(0, knowledgeBaseGenerator.getKnowledgeBaseInfo().getGlobalFilteringRuleIds().size());
    }

    /**
     * Test to verify that knowledge base is generated correctly for a rule config with multiple rules.
     *
     * @throws MalformedConfigurationException if an error occurs in rule generation
     */
    @Test
    public void should_generate_knowledge_base_correctly_for_a_multiple_rules() throws MalformedConfigurationException {
        knowledgeBaseGenerator.setConfigurations(VALID_RULE_CONFIG_WITH_MULTIPLE_RULES);
        assertNotNull(knowledgeBaseGenerator.getKnowledgeBaseInfo());
        assertNotNull(knowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase());

        assertEquals(4, knowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase().getPackage("mrulespack").getRules().size());
        assertEquals(2, knowledgeBaseGenerator.getKnowledgeBaseInfo().getGlobalFilteringRuleIds().size());
    }
}
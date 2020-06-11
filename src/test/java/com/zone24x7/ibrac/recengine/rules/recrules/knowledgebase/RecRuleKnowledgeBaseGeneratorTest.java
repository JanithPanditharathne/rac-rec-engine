package com.zone24x7.ibrac.recengine.rules.recrules.knowledgebase;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.exceptions.RuleGeneratorException;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RecConfig;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.recrules.rulegenerators.DroolsRecRuleGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for RecRuleKnowledgeBaseGenerator
 */
class RecRuleKnowledgeBaseGeneratorTest {

    private RecRuleKnowledgeBaseGenerator recRuleKnowledgeBaseGenerator;
    private Logger logger;
    private Validator validator;
    private DroolsRecRuleGenerator recRuleGenerator;
    private static final String EXAMPLE_DRL = "package recrulespack\n\n" +
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
    private static final String EXAMPLE_WRONG_DRL = "package recrulespack\n\n" +
            "import java.util.List;\n" +
            "import com.zone24x7.ibrac.recengine.pojo.rules.MatchingCondition;\n" +
            "\nrule \"100\"\n" +
            "when\n" +
            "    $recs: List();\n" +
            "    RecRuleKnowledgeBaseInfo($recIdToRecMap : recIdToRecMap);\n" +
            "    MatchingCondition(matchingMap[\"department\"] equalsIgnoreCase \"Clothing\" && matchingMap[\"brand\"] equalsIgnoreCase \"Tommy\")\n" +
            "then\n" +
            "    $recs.add($recIdToRecMap.get(\"100\"));\n" +
            "end";
    private RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo;


    @BeforeEach
    void setUp() throws RuleGeneratorException {
        recRuleKnowledgeBaseGenerator = new RecRuleKnowledgeBaseGenerator();
        logger = mock(Logger.class);
        recRuleGenerator = mock(DroolsRecRuleGenerator.class);
        validator = spy(Validation.buildDefaultValidatorFactory().getValidator());
        recRuleKnowledgeBaseInfo = spy(RecRuleKnowledgeBaseInfo.class);

        ReflectionTestUtils.setField(recRuleKnowledgeBaseGenerator, "recRuleGenerator", recRuleGenerator);
        ReflectionTestUtils.setField(recRuleKnowledgeBaseGenerator, "validator", validator);
        ReflectionTestUtils.setField(recRuleKnowledgeBaseGenerator, "recRuleKnowledgeBaseInfo", recRuleKnowledgeBaseInfo);

        when(recRuleGenerator.generateRecRule(anyString(), anyString())).thenReturn(EXAMPLE_DRL);
    }

    /**
     * Test to verify that knowledge base info is not created when rule config is empty.
     */
    @Test
    public void should_throw_exception_when_rec_rule_config_is_empty_or_null() {
        assertThrows(MalformedConfigurationException.class, () -> recRuleKnowledgeBaseGenerator.setConfigurations(""));
        assertThrows(MalformedConfigurationException.class, () -> recRuleKnowledgeBaseGenerator.setConfigurations(null));
    }

    /**
     * should throw MalformedConfigurationException if non json input is received
     */
    @Test
    void should_throw_MalformedConfigurationException_if_non_json_input_is_received() throws MalformedConfigurationException {
        assertThrows(MalformedConfigurationException.class, () -> recRuleKnowledgeBaseGenerator.setConfigurations("<html></html>"));
    }

    /**
     * should throw MalformedConfigurationException if non recs node is null
     */
    @Test
    void should_throw_MalformedConfigurationException_if_recs_node_is_null() throws MalformedConfigurationException {
        assertThrows(MalformedConfigurationException.class, () -> recRuleKnowledgeBaseGenerator.setConfigurations("{\"recs\":null}"));
    }

    /**
     * should throw MalformedConfigurationException if invalid rec object is found
     */
    @Test
    void should_throw_MalformedConfigurationException_when_invalid_rec_object_is_found() {
        assertThrows(MalformedConfigurationException.class, () ->
                //Sample Rec Config 2 does not have a id
                recRuleKnowledgeBaseGenerator.setConfigurations(
                        "{\"recs\":[{\"id\":\"100\",\"name\":\"Sample Rec Config 1\",\"type\":\"REGULAR\",\"matchingCondition\":null," +
                                "\"regularConfig\":{\"bundleId\":\"1201\"},\"testConfig\":null},{\"name\":\"Sample Rec Config 2\"," +
                                "\"type\":\"REGULAR\",\"matchingCondition\":null,\"regularConfig\":{\"bundleId\":\"1202\"}," +
                                "\"testConfig\":null}]}")
        );
    }

    /**
     * returned knowledgebase must have all required fields set in happy path
     */
    @Test
    void returned_knowledge_base_must_have_all_required_fields_set_in_happy_path() throws MalformedConfigurationException, RuleGeneratorException {
        recRuleKnowledgeBaseGenerator.setConfigurations("{\"recs\":[{\"id\":\"100\",\"name\":\"Sample Rec Config 1\",\"type\":\"REGULAR\",\"matchingCondition\":\"department #= \\\"Clothing\\\" && brand #= \\\"Tommy\\\"\",\"regularConfig\":{\"bundleId\":\"1201\"},\"testConfig\":null}]}");
        verify(recRuleGenerator).generateRecRule("100", "department #= \"Clothing\" && brand #= \"Tommy\"");

        RecRuleKnowledgeBaseInfo knowledgeBaseInfo = recRuleKnowledgeBaseGenerator.getKnowledgeBaseInfo();

        assertThat(knowledgeBaseInfo, is(notNullValue()));
        assertThat(recRuleKnowledgeBaseGenerator.getKnowledgeBaseInfo().getKnowledgeBase(), is(notNullValue()));
        assertThat(recRuleKnowledgeBaseGenerator.getKnowledgeBaseInfo().getRecIdToRecMap(), is(notNullValue()));
        assertThat(recRuleKnowledgeBaseGenerator.getKnowledgeBaseInfo().getRecIdToRecMap().size(), equalTo(1));
    }

    /**
     * should throw malformed exception when knowledge base has error
     */
    @Test
    void should_throw_MalformedConfigurationException_when_knowledge_base_has_error() throws MalformedConfigurationException, RuleGeneratorException {
        //Return invalid drl with removed import import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo
        when(recRuleGenerator.generateRecRule(anyString(), anyString())).thenReturn(EXAMPLE_WRONG_DRL);

        assertThrows(MalformedConfigurationException.class, () ->
                recRuleKnowledgeBaseGenerator
                        .setConfigurations("{\"recs\":[{\"id\":\"100\",\"name\":\"Sample Rec Config 1\",\"type\":\"REGULAR\",\"matchingCondition\":\"department #= \\\"Clothing\\\" && brand #= \\\"Tommy\\\"\",\"regularConfig\":{\"bundleId\":\"1201\"},\"testConfig\":null}]}")
        );
    }

    /**
     * should throw malformed exception when recGenerator call
     */
    @Test
    void should_throw_MalformedConfigurationException_when_recGenerator_throws_exception() throws MalformedConfigurationException, RuleGeneratorException {
        //Throw exception when generate method is called
        when(recRuleGenerator.generateRecRule(anyString(), anyString())).thenThrow(new RuleGeneratorException(""));

        assertThrows(MalformedConfigurationException.class, () ->
                recRuleKnowledgeBaseGenerator
                        .setConfigurations("{\"recs\":[{\"id\":\"100\",\"name\":\"Sample Rec Config 1\",\"type\":\"REGULAR\",\"matchingCondition\":\"department #= \\\"Clothing\\\" && brand #= \\\"Tommy\\\"\",\"regularConfig\":{\"bundleId\":\"1201\"},\"testConfig\":null}]}")
        );
    }

    /**
     * should throw malformed exception when validator throws validation exception
     */
    @Test
    void should_throw_MalformedConfigurationException_when_validator_throws_validation_exception() throws MalformedConfigurationException, RuleGeneratorException {
        validator = mock(Validator.class);
        ReflectionTestUtils.setField(recRuleKnowledgeBaseGenerator, "validator", validator);

        when(validator.validate(any(RecConfig.class))).thenThrow(new ValidationException());
        //Throw exception when generate method is called
        //  when(recRuleGenerator.generateRecRule(anyString(), anyString())).thenThrow(new RuleGeneratorException(""));

        assertThrows(MalformedConfigurationException.class, () ->
                recRuleKnowledgeBaseGenerator
                        .setConfigurations("{\"recs\":[{\"id\":\"100\",\"name\":\"Sample Rec Config 1\",\"type\":\"REGULAR\",\"matchingCondition\":\"department #= \\\"Clothing\\\" && brand #= \\\"Tommy\\\"\",\"regularConfig\":{\"bundleId\":\"1201\"},\"testConfig\":null}]}")
        );
    }
}
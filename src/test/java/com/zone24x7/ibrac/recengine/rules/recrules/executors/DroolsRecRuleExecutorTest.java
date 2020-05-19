package com.zone24x7.ibrac.recengine.rules.recrules.executors;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rec;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.customoperators.StringEvaluatorDefinition;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.api.definition.KiePackage;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.builder.conf.EvaluatorOption;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;

/**
 * Test class for DroolsRecRuleExecutor
 */
class DroolsRecRuleExecutorTest {
    private RecCycleStatus recCycleStatus;
    private DroolsRecRuleExecutor ruleExecutor;
    private InternalKnowledgeBase knowledgeBase;
    private Rec rec1;
    private Rec rec2;
    private Rec rec3;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        recCycleStatus = mock(RecCycleStatus.class);
        ruleExecutor = new DroolsRecRuleExecutor();
        ReflectionTestUtils.setField(ruleExecutor, "logger", mock(Logger.class));

        // Create knowledge builder configs
        KnowledgeBuilderConfiguration builderConf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        builderConf.setOption(EvaluatorOption.get("equalsIgnoreCase", new StringEvaluatorDefinition()));

        // Create knowledge builder
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(builderConf);
        knowledgeBuilder.add(ResourceFactory.newFileResource(Paths.get("").toAbsolutePath() + "/src/test/java/com/zone24x7/ibrac/recengine/rules/recrules/executors/RecTestRules.drl"), ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            System.out.println(knowledgeBuilder.getErrors().toString());
        }
        Collection<KiePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();

        // Create knowledge base
        knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addPackages(knowledgePackages);

        rec1 = new Rec();
        rec1.setId("100");
        rec1.setMatchingCondition("(department == \"Shoes\" || (department == \"Clothing\" && brand == \"Tommy\"))");

        rec2 = new Rec();
        rec2.setId("101");
        rec2.setMatchingCondition("(department == \"Shoes\" || (department == \"Clothing\" && brand == \"Tommy\"))");

        rec3 = new Rec();
        rec3.setId("102");
        rec3.setMatchingCondition("department == \"Electronics\"");

        //Map
        Map<String, Rec> recMap = new HashMap<>();
        recMap.put("100", rec1);
        recMap.put("101", rec2);
        recMap.put("102", rec3);

        // Create knowledge base information
        RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo = new RecRuleKnowledgeBaseInfo();
        recRuleKnowledgeBaseInfo.setKnowledgeBase(knowledgeBase);
        recRuleKnowledgeBaseInfo.setRecIdToRecMap(recMap);

        ruleExecutor.setRecRuleKnowledgeBaseInfo(recRuleKnowledgeBaseInfo);
    }

    /**
     * Should return and empty list if knowledge base is null
     */
    @Test
    void should_return_empty_list_when_kb_is_null() {
        RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo = new RecRuleKnowledgeBaseInfo();
        recRuleKnowledgeBaseInfo.setKnowledgeBase(null);
        ruleExecutor.setRecRuleKnowledgeBaseInfo(recRuleKnowledgeBaseInfo);

        // Empty recIds
        Set<String> recIds = new HashSet<>();

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "Clothing");
        ccp.put("brand", "Tommy");

        List<Rec> matchingRecs = ruleExecutor.getMatchingRecs(recIds, ccp, recCycleStatus);

        assertThat(matchingRecs, empty());
    }

    /**
     * Should return and empty list if incoming recIds are empty
     */
    @Test
    void should_return_empty_list_when_recIds_are_empty() {
        // Empty recIds
        Set<String> recIds = new HashSet<>();

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "Clothing");
        ccp.put("brand", "Tommy");

        List<Rec> matchingRecs = ruleExecutor.getMatchingRecs(recIds, ccp, recCycleStatus);

        assertThat(matchingRecs, empty());
    }

    /**
     * Should return all recIds when condition meets condition
     */
    @Test
    void should_return_matching_recs_when_ccp_satisfy_conditions_in_all_recs() {
        Set<String> recIds = new HashSet<>();
        recIds.add("100");
        recIds.add("101");
        recIds.add("102");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "Clothing");
        ccp.put("brand", "Tommy");

        List<Rec> matchingRecs = ruleExecutor.getMatchingRecs(recIds, ccp, recCycleStatus);

        assertThat(matchingRecs, hasSize(3));
        assertThat(matchingRecs, hasItem(rec1));
        assertThat(matchingRecs, hasItem(rec2));
        assertThat(matchingRecs, hasItem(rec3));
    }

    /**
     * Should return matching recs only
     */
    @Test
    void should_return_matching_recs_when_ccp_satisfy_conditions_in_recs_some_recs() {
        Set<String> recIds = new HashSet<>();
        recIds.add("100");
        recIds.add("101");
        recIds.add("102");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "Shoes");
        ccp.put("brand", "Tommy");

        List<Rec> matchingRecs = ruleExecutor.getMatchingRecs(recIds, ccp, recCycleStatus);

        assertThat(matchingRecs, hasSize(2));
        assertThat(matchingRecs, hasItem(rec2));
        assertThat(matchingRecs, hasItem(rec3));
    }

    /**
     * Should only consider given recIds. drl file contains 2 recs with conditions matched.
     * But it should be only 1 rec as only 1 matches from given recIds.
     */
    @Test
    void should_only_consider_given_rec_ids_for_matching() {
        Set<String> recIds = new HashSet<>();
        recIds.add("100");
        recIds.add("102");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "Shoes");
        ccp.put("brand", "Tommy");

        List<Rec> matchingRecs = ruleExecutor.getMatchingRecs(recIds, ccp, recCycleStatus);

        assertThat(matchingRecs, hasSize(1));
        assertThat(matchingRecs, hasItem(rec3));
    }
}
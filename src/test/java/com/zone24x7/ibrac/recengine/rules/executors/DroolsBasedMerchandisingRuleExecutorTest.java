package com.zone24x7.ibrac.recengine.rules.executors;

import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.rules.EdeExecutedRuleInfo;
import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;
import com.zone24x7.ibrac.recengine.pojo.rules.MerchandisingRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.customoperators.StringEvaluatorDefinition;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Class to test the drools base merchandising rule executor.
 */
public class DroolsBasedMerchandisingRuleExecutorTest {
    private DroolsBasedMerchandisingRuleExecutor ruleExecutor;
    private Product product1 = null;
    private Product product2 = null;
    private Product product3 = null;
    private Product product4 = null;
    private Product product5 = null;
    private Product product6 = null;
    private Product product7 = null;
    private Product product8 = null;
    private List<Product> productList;
    private RecCycleStatus recCycleStatus;

    private InternalKnowledgeBase knowledgeBase;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        recCycleStatus = mock(RecCycleStatus.class);
        ruleExecutor = new DroolsBasedMerchandisingRuleExecutor();
        ReflectionTestUtils.setField(ruleExecutor, "logger", mock(Logger.class));

        // Create knowledge builder configs
        KnowledgeBuilderConfiguration builderConf = KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        builderConf.setOption(EvaluatorOption.get("equalsIgnoreCase", new StringEvaluatorDefinition()));

        // Create knowledge builder
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder(builderConf);
        knowledgeBuilder.add(ResourceFactory.newFileResource(Paths.get("").toAbsolutePath() + "/src/test/java/com/zone24x7/ibrac/recengine/rules/executors/MerchandisingTestRules.drl"), ResourceType.DRL);
        Collection<KiePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();

        // Create knowledge base
        knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addPackages(knowledgePackages);

        // Create knowledge base information
        MerchandisingRuleKnowledgeBaseInfo knowledgeBaseInfo = new MerchandisingRuleKnowledgeBaseInfo();
        knowledgeBaseInfo.setKnowledgeBase(knowledgeBase);

        //knowledgeBaseInfo.addGlobalFilteringRuleIds();
        ruleExecutor.setKnowledgeBaseInfo(knowledgeBaseInfo);

        product1 = new Product();
        product1.setAttributesMap(new HashMap<>());
        product2 = new Product();
        product2.setAttributesMap(new HashMap<>());
        product3 = new Product();
        product3.setAttributesMap(new HashMap<>());
        product4 = new Product();
        product4.setAttributesMap(new HashMap<>());
        product5 = new Product();
        product5.setAttributesMap(new HashMap<>());
        product6 = new Product();
        product6.setAttributesMap(new HashMap<>());
        product7 = new Product();
        product7.setAttributesMap(new HashMap<>());
        product8 = new Product();
        product8.setAttributesMap(new HashMap<>());

        product1.getAttributesMap().put("id", "1");
        product2.getAttributesMap().put("id", "2");
        product3.getAttributesMap().put("id", "3");
        product4.getAttributesMap().put("id", "4");
        product5.getAttributesMap().put("id", "5");
        product6.getAttributesMap().put("id", "6");
        product7.getAttributesMap().put("id", "7");
        product8.getAttributesMap().put("id", "8");

        product1.getAttributesMap().put("department", "electronics");
        product2.getAttributesMap().put("department", "clothing");
        product2.getAttributesMap().put("category", "tops");
        product2.getAttributesMap().put("brand", "adidas");
        product3.getAttributesMap().put("department", "electronics");
        product4.getAttributesMap().put("department", "shoes");
        product5.getAttributesMap().put("department", "shoes");
        product6.getAttributesMap().put("department", "clothing");
        product6.getAttributesMap().put("category", "tops");
        product6.getAttributesMap().put("brand", "nike");
        product7.getAttributesMap().put("department", "clothing");
        product8.getAttributesMap().put("department", "shoes");

        productList = new LinkedList<>();
        productList.add(product1);
        productList.add(product2);
        productList.add(product3);
        productList.add(product4);
        productList.add(product5);
        productList.add(product6);
        productList.add(product7);
        productList.add(product8);
    }

    /**
     * Test to verify that non filtered recommendations are sent when the knowledge base is null.
     */
    @Test
    public void should_return_the_non_filtered_recommendations_when_knowledge_base_is_null() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("1");

        DroolsBasedMerchandisingRuleExecutor ruleExecutorWithNoKb = new DroolsBasedMerchandisingRuleExecutor();
        ruleExecutorWithNoKb.setKnowledgeBaseInfo(null);

        FilteringRulesResult filteredRecommendations = ruleExecutorWithNoKb.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that non filtered recommendations are sent when the rule ids to execute are empty.
     */
    @Test
    public void should_return_the_non_filtered_recommendations_when_rules_ids_are_empty() {
        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, new HashSet<>(), new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products with department shoes are boosted correctly.
     */
    @Test
    public void should_boost_products_with_shoes_department_correctly() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("1");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "1");
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "4", "5", "8", "1", "2", "3", "6", "7");
    }

    /**
     * Test to verify that products are boosted with complex conditions.
     */
    @Test
    public void should_boost_products_are_boosted_correctly_with_complex_conditions() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports");

        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("2");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "2");
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "4", "5", "6", "8", "1", "2", "3", "7");
    }

    /**
     * Test to verify that products are not boosted boosted when matching condition is not met.
     */
    @Test
    public void should_not_boost_products_when_matching_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("2");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not boosted boosted when matching condition is not met.
     */
    @Test
    public void should_not_boost_products_when_matching_condition_is_not_met2() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("2");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "abcd");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products with department shoes are boosted correctly with limit.
     */
    @Test
    public void should_boost_products_with_shoes_department_correctly_with_limit() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("3");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "3");
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "4", "5", "1", "2", "3", "6", "7", "8");
    }

    /**
     * Test to verify that global rules are executed.
     */
    @Test
    public void should_execute_global_rules() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("3");

        MerchandisingRuleKnowledgeBaseInfo knowledgeBaseInfo = new MerchandisingRuleKnowledgeBaseInfo();
        knowledgeBaseInfo.setKnowledgeBase(knowledgeBase);
        knowledgeBaseInfo.addGlobalFilteringRuleIds(new LinkedHashSet<>(Arrays.asList("1")));
        ruleExecutor.setKnowledgeBaseInfo(knowledgeBaseInfo);

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 2, "1", "3");
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "4", "5", "8", "1", "2", "3", "6", "7");
    }

    /**
     * Test to verify that products are not boosted when products are not matched.
     */
    @Test
    public void should_not_boost_products_when_product_criteria_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("4");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products with department shoes are buried correctly.
     */
    @Test
    public void should_bury_products_with_shoes_department_correctly() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("5");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "5");
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "6", "7", "4", "5", "8");
    }

    /**
     * Test to verify that products are buried with complex conditions.
     */
    @Test
    public void should_bury_products_correctly_with_complex_conditions() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports");

        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("6");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "6");
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "7", "4", "5", "6", "8");
    }

    /**
     * Test to verify that products are not buried when matching condition is not met.
     */
    @Test
    public void should_not_bury_products_when_matching_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("6");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not buried when matching condition is not met.
     */
    @Test
    public void should_not_bury_products_when_matching_condition_is_not_met2() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("6");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "abcd");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not buried when action condition is not met.
     */
    @Test
    public void should_not_bury_products_when_action_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("7");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products with department shoes are only recommended correctly.
     */
    @Test
    public void should_only_recommend_products_with_shoes_department_correctly() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("8");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "8");
        assertProductsInFilteringRuleResult(filteredRecommendations, 3, "4", "5", "8");
    }

    /**
     * Test to verify that products are only recommended with complex conditions.
     */
    @Test
    public void should_only_recommend_products_correctly_with_complex_conditions() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports");

        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("9");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "9");
        assertProductsInFilteringRuleResult(filteredRecommendations, 4, "4", "5", "6", "8");
    }

    /**
     * Test to verify that products are not changed and only recommend rule is not executed when matching condition is not met.
     */
    @Test
    public void should_not_only_recommend_products_when_matching_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("9");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not changed and only recommend rule is not executed when matching condition is not met.
     */
    @Test
    public void should_not_only_recommend_products_when_matching_condition_is_not_met2() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("9");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "abcd");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not recommended when action condition is not met.
     */
    @Test
    public void should_not_recommend_any_product_when_action_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("10");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "10");
        assertProductsInFilteringRuleResult(filteredRecommendations, 0);
    }

    /**
     * Test to verify that products with department shoes are not recommended correctly.
     */
    @Test
    public void should_not_recommend_products_with_shoes_department_correctly() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("11");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "11");
        assertProductsInFilteringRuleResult(filteredRecommendations, 5, "1", "2", "3", "6", "7");
    }

    /**
     * Test to verify that products are not recommended with complex conditions.
     */
    @Test
    public void should_not_recommend_products_correctly_with_complex_conditions() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports");

        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("12");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 1, "12");
        assertProductsInFilteringRuleResult(filteredRecommendations, 4, "1", "2", "3", "7");
    }

    /**
     * Test to verify that products are not changed and do not recommend rule is not executed when matching condition is not met.
     */
    @Test
    public void should_not_do_not_recommend_products_when_matching_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("12");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not changed and do not recommend rule is not executed when matching condition is not met.
     */
    @Test
    public void should_not_do_not_recommend_products_when_matching_condition_is_not_met2() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("12");

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "abcd");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, ccp, recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Test to verify that products are not changed and do not recommend rule is not executed when action condition is not met.
     */
    @Test
    public void should_not_change_products_when_action_condition_is_not_met() {
        Set<String> ruleIds = new HashSet<>();
        ruleIds.add("13");

        FilteringRulesResult filteredRecommendations = ruleExecutor.getFilteredRecommendations(productList, ruleIds, new HashMap<>(), recCycleStatus);
        assertRuleInfoInFilteringRuleResult(filteredRecommendations, 0);
        assertProductsInFilteringRuleResult(filteredRecommendations, 8, "1", "2", "3", "4", "5", "6", "7", "8");
    }

    /**
     * Method to assert the rules in a filtering rule result.
     *
     * @param filteringRulesResult      the filtering rule result
     * @param expectedExecutedRuleCount the expected rule count
     * @param expectedExecutedRuleIds   the expected rule ids
     */
    private void assertRuleInfoInFilteringRuleResult(FilteringRulesResult filteringRulesResult, int expectedExecutedRuleCount, String... expectedExecutedRuleIds) {
        assertEquals(expectedExecutedRuleCount, filteringRulesResult.getExecutedFilteringRuleInfo().size());

        if (expectedExecutedRuleCount > 0) {
            Iterator<EdeExecutedRuleInfo> iterator = filteringRulesResult.getExecutedFilteringRuleInfo().iterator();
            List<String> ruleIdsList = Arrays.asList(expectedExecutedRuleIds);
            for (int i = 0; i < expectedExecutedRuleCount; i++) {
                assertTrue(ruleIdsList.contains(iterator.next().getRuleId()));
            }
        }
    }

    /**
     * Method to assert the products in a filtering rule result.
     *
     * @param filteringRulesResult the filtering rule result
     * @param expectedProductCount the expected product count
     * @param expectedProductIds   the expected product ids
     */
    private void assertProductsInFilteringRuleResult(FilteringRulesResult filteringRulesResult, int expectedProductCount, String... expectedProductIds) {
        assertEquals(expectedProductCount, filteringRulesResult.getFilteredRecommendedProductsList().size());

        if (expectedProductCount > 0) {
            Iterator<Product> iterator = filteringRulesResult.getFilteredRecommendedProductsList().iterator();

            for (int i = 0; i < expectedProductCount; i++) {
                assertEquals(expectedProductIds[i], iterator.next().getAttributesMap().get("id"));
            }
        }
    }
}
package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class of RecAlgorithmCombinator
 */
class RecAlgorithmCombinatorTest {

    private RecAlgorithmCombinator recAlgorithmCombinator;
    private AlgorithmTaskFactory algorithmTaskFactory;
    private ExecutorService cachedTaskExecutorService;
    private AlgoCombineInfo algoCombineInfo;
    private Set<String> rules = new HashSet<>();

    private List<BundleAlgorithm> bundleAlgorithms = new LinkedList<>();

    private BundleAlgorithm algorithm1;
    private BundleAlgorithm algorithm2;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;

    private List<Product> outputProducts1 = new LinkedList<>();
    private List<Product> outputProducts2 = new LinkedList<>();

    private AlgorithmResult algorithmResult1 = new AlgorithmResult();
    private AlgorithmResult algorithmResult2 = new AlgorithmResult();

    String algorithmId1 = "100";
    String algorithmId2 = "101";
    Map<String, String> ccp = new LinkedHashMap<>();
    RecInputParams recInputParams = new RecInputParams();
    RecCycleStatus recCycleStatus = new RecCycleStatus("1234");

    Future<AlgorithmResult> future1;
    Future<AlgorithmResult> future2;

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        recAlgorithmCombinator = new RecAlgorithmCombinator();

        algorithmTaskFactory = mock(AlgorithmTaskFactory.class);
        cachedTaskExecutorService = mock(ExecutorService.class);
        ReflectionTestUtils.setField(recAlgorithmCombinator, "algorithmTaskFactory", algorithmTaskFactory);
        ReflectionTestUtils.setField(recAlgorithmCombinator, "cachedTaskExecutorService", cachedTaskExecutorService);

        algoCombineInfo = new AlgoCombineInfo();

        algorithm1 = new BundleAlgorithm();
        algorithm1.setId("100");
        algorithm1.setCustomDisplayText("algo1DisplayText");

        bundleAlgorithms.add(algorithm1);

        algorithm2 = new BundleAlgorithm();
        algorithm2.setId("101");
        algorithm2.setCustomDisplayText("algo2DisplayText");

        bundleAlgorithms.add(algorithm2);

        product1 = new Product();
        product1.setProductId("1");

        product2 = new Product();
        product2.setProductId("2");

        product3 = new Product();
        product3.setProductId("3");

        product4 = new Product();
        product4.setProductId("4");

        outputProducts1.add(product1);
        algorithmResult1.setRecProducts(outputProducts1);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("productNumbers", "2213222");
        algorithmResult1.setUsedCcp(map);

        outputProducts2.add(product2);
        outputProducts2.add(product3);
        algorithmResult2.setRecProducts(outputProducts2);

        AlgorithmTask algorithmTask1 = mock(AlgorithmTask.class);
        AlgorithmTask algorithmTask2 = mock(AlgorithmTask.class);

        future1 = mock(Future.class);
        future2 = mock(Future.class);

        ccp = new LinkedHashMap<>();
        recInputParams = new RecInputParams();

        recInputParams.setCcp(ccp);

        when(algorithmTaskFactory.create(algorithmId1, ccp, recCycleStatus)).thenReturn(algorithmTask1);
        when(algorithmTaskFactory.create(algorithmId2, ccp, recCycleStatus)).thenReturn(algorithmTask2);

        when(cachedTaskExecutorService.submit(algorithmTask1)).thenReturn(future1);
        when(cachedTaskExecutorService.submit(algorithmTask2)).thenReturn(future2);

        when(future1.get()).thenReturn(algorithmResult1); // 2 products
        when(future2.get()).thenReturn(algorithmResult2); // 1 product
    }

    /**
     * When combine= false and given limit is not satisfied, should return from the algo producing max results
     */
    @Test
    void should_return_max_no_products_when_limit_is_not_satisfied_when_combine_false() {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(false);

        int limit = 3;

        ActiveBundle activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "", "FLAT"), "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("101"), is(""));
        assertThat(combinedAlgoResult.getDisplayText(), is("algo2DisplayText"));
    }

    /**
     * When combine= false and given limit is satisfied by first algo, should return from that algo
     */
    @Test
    void should_return_from_the_first_algo_satisfy_the_limit_when_combine_false() {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(false);

        int limit = 1; // algo 100 satisfies

        ActiveBundle activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "", "FLAT"), "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("100"), is("1"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("100"), is("productNumbers:2213222"));
        assertThat(combinedAlgoResult.getDisplayText(), is("algo1DisplayText"));
    }

    /**
     * When combine = true and given limit is satisfied by first algo, should return from that algo
     */
    @Test
    void should_return_from_the_first_algo_satisfy_the_limit_when_combine_true() {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(true);
        algoCombineInfo.setCombineDisplayText("CombinedDisplayText");

        int limit = 1; // algo 100 satisfies

        ActiveBundle activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "", "FLAT"), "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("100"), is("1"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("100"), is("productNumbers:2213222"));
        assertThat(combinedAlgoResult.getDisplayText(), is("CombinedDisplayText"));
    }

    /**
     * When combine = true and given limit is not satisfied by one algo, should return from the combined result.
     * No limiting should happen on the combined result even if more products are there.
     */
    @Test
    void should_return_combined_result_when_satisfy_the_limit_by_multiple_algos_when_combine_true() {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(true);
        algoCombineInfo.setCombineDisplayText("CombinedDisplayText");

        int limit = 2; // 1 product from algo 100 and one product from algo 101 satisfies limit=2

        ActiveBundle activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "", "FLAT"), "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(3)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("100"), is("1"));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(2));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("100"), is("productNumbers:2213222"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("101"), is(""));
        assertThat(combinedAlgoResult.getDisplayText(), is("CombinedDisplayText"));
    }

    /**
     * Should return a not null MultipleAlgoResult_ when algoCombine info is null
     */
    @Test
    void should_return_not_null_MultipleAlgoResult_object_when_AlgoCombineInfo_is_null() {
        int limit = 3;
        ActiveBundle activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "", "FLAT"), "1", limit, bundleAlgorithms, null, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult, is(notNullValue()));
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(0)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(0)));
    }

    /**
     * Should return a not null MultipleAlgoResult_ when validAlgorithmListToExecute info is null
     */
    @Test
    void should_return_not_null_MultipleAlgoResult_object_when_validAlgorithmListToExecute_is_null() {
        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(true);
        algoCombineInfo.setCombineDisplayText("CombinedDisplayText");

        int limit = 3;
        //Seeting null to valid algos to execute
        ActiveBundle activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "", "FLAT"), "1", limit, null, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult, is(notNullValue()));
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(0)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(0)));
    }

    /**
     * Should log and ignore if algo result generation failed with  ExecutionException
     * Using combine flow
     */
    @Test
    void should_log_and_ignore_if_algo_result_generation_failed_with_ExecutionException_combined() throws ExecutionException, InterruptedException {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(true);
        algoCombineInfo.setCombineDisplayText("CombinedDisplayText");

        int limit = 2; // 1 product from algo 100 and 2 products from algo 101 satisfies limit=2. Since ExecutionException in algo1 all should be from algo 2

        when(future1.get()).thenThrow(new ExecutionException(new Exception()));

        //Seeting null to valid algos to execute
        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);

        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("101"), is(""));
        assertThat(combinedAlgoResult.getDisplayText(), is("CombinedDisplayText"));
    }

    /**
     * Should log and ignore if algo result generation failed with  ExecutionException
     * Using combine flow
     */
    @Test
    void should_log_and_ignore_if_algo_result_generation_failed_with_InterruptedException_combined() throws ExecutionException, InterruptedException {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(true);
        algoCombineInfo.setCombineDisplayText("CombinedDisplayText");

        int limit = 2; // 1 product from algo 100 and 2 products from algo 101 satisfies limit=2. Since ExecutionException in algo1 all should be from algo 2

        when(future1.get()).thenThrow(new InterruptedException());

        //Seeting null to valid algos to execute
        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);

        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("101"), is(""));
        assertThat(combinedAlgoResult.getDisplayText(), is("CombinedDisplayText"));
    }

    /**
     * Should log and ignore if algo result generation failed with  ExecutionException
     * Using combine flow
     */
    @Test
    void should_log_and_ignore_if_algo_result_generation_failed_with_ExecutionException_non_combined() throws ExecutionException, InterruptedException {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(false);

        int limit = 2; // 1 product from algo 100 and 2 products from algo 101 satisfies limit=2. Since ExecutionException in algo1 all should be from algo 2

        when(future1.get()).thenThrow(new ExecutionException(new Exception()));

        //Seeting null to valid algos to execute
        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);

        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("101"), is(""));
        assertThat(combinedAlgoResult.getDisplayText(), is("algo2DisplayText"));
    }

    /**
     * Should log and ignore if algo result generation failed with  ExecutionException
     * Using combine flow
     */
    @Test
    void should_log_and_ignore_if_algo_result_generation_failed_with_InterruptedException_non_combined() throws ExecutionException, InterruptedException {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(false);

        int limit = 2; // 1 product from algo 100 and 2 products from algo 101 satisfies limit=2. Since ExecutionException in algo1 all should be from algo 2

        when(future1.get()).thenThrow(new InterruptedException());

        //Seeting null to valid algos to execute
        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);

        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().size(), is(1));
        assertThat(combinedAlgoResult.getAlgoToUsedCcp().get("101"), is(""));
        assertThat(combinedAlgoResult.getDisplayText(), is("algo2DisplayText"));
    }

    /**
     * should use the default display text when no custom display text.
     * Using the unittest written for below scenario
     * When combine= false and given limit is satisfied by first algo, should return from that algo
     */
    @Test
    void should_use_the_default_display_text_when_no_custom_display_text() {
        //one product from algo 100
        //two products from algo 101

        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setEnableCombine(false);

        int limit = 1; // algo 100 satisfies

        //Setting custom display text
        BundleAlgorithm bundleAlgorithm = bundleAlgorithms.get(0);
        bundleAlgorithm.setCustomDisplayText(null);
        bundleAlgorithm.setDefaultDisplayText("algo1DefaultDisplayText");

        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getDisplayText(), is("algo1DefaultDisplayText"));
    }
}

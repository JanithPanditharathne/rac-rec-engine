package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @BeforeEach
    void setUp() throws ExecutionException, InterruptedException {
        recAlgorithmCombinator = new RecAlgorithmCombinator();

        algorithmTaskFactory = mock(AlgorithmTaskFactory.class);
        cachedTaskExecutorService = mock(ExecutorService.class);
        ReflectionTestUtils.setField(recAlgorithmCombinator, "logger", mock(Logger.class));
        ReflectionTestUtils.setField(recAlgorithmCombinator, "algorithmTaskFactory", algorithmTaskFactory);
        ReflectionTestUtils.setField(recAlgorithmCombinator, "cachedTaskExecutorService", cachedTaskExecutorService);

        algoCombineInfo = new AlgoCombineInfo();

        algorithm1 = new BundleAlgorithm();
        algorithm1.setId("100");
        bundleAlgorithms.add(algorithm1);

        algorithm2 = new BundleAlgorithm();
        algorithm2.setId("101");
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

        outputProducts2.add(product2);
        outputProducts2.add(product3);
        algorithmResult2.setRecProducts(outputProducts2);

        AlgorithmTask algorithmTask1 = mock(AlgorithmTask.class);
        AlgorithmTask algorithmTask2 = mock(AlgorithmTask.class);

        Future<AlgorithmResult> future1 = mock(Future.class);
        Future<AlgorithmResult> future2 = mock(Future.class);

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

        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));

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

        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("100"), is("1"));
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

        int limit = 1; // algo 100 satisfies

        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);
        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(1)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("100"), is("1"));
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
        int limit = 2; // 1 product from algo 100 and one product from algo 101 satisfies limit=2

        ActiveBundle activeBundle = new ActiveBundle("1", "", "FLAT", "1", limit, bundleAlgorithms, algoCombineInfo, rules);
        MultipleAlgorithmResult combinedAlgoResult = recAlgorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus);

        assertThat(combinedAlgoResult.getRecProducts().size(), is(equalTo(3)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().size(), is(equalTo(2)));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("100"), is("1"));
        assertThat(combinedAlgoResult.getAlgoToProductsMap().get("101"), is("2,3"));
    }
}
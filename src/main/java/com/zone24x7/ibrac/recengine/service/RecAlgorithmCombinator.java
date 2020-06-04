package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Exposes methods to combine results from multiple algorithms according to the provided strategy.
 */
@Component
public class RecAlgorithmCombinator implements AlgorithmCombinator {

    @Autowired
    private AlgorithmTaskFactory algorithmTaskFactory;

    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

    @Log
    private Logger logger;



    /**
     * Combines results from multiple algorithms according to the provided strategy.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algorithm execution.
     * @param recCycleStatus recCycle status.
     * @return a combined algorithm Result.
     */
    @Override
    public MultipleAlgorithmResult getCombinedAlgoResult(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        AlgoCombineInfo algoCombineInfo = activeBundle.getAlgoCombineInfo();
        List<BundleAlgorithm> validAlgorithmListToExecute = activeBundle.getValidAlgorithmListToExecute();

        if (validAlgorithmListToExecute == null
                || CollectionUtils.isEmpty(validAlgorithmListToExecute)
                || algoCombineInfo == null) {
            logger.error("Required information not available for algorithm execution and combination. " +
                                 "AlgoCombineInfo: {}, ValidAlgorithmListToExecute: {}", algoCombineInfo, validAlgorithmListToExecute);
            return new MultipleAlgorithmResult();
        }

        Map<String, Future<AlgorithmResult>> futures = new LinkedHashMap<>();
        Map<String, String> algoIdToDisplayText = new LinkedHashMap<>();

        //TODO: Change logic to limit HBase load by calling batch wise as a optimization
        for (BundleAlgorithm bundleAlgorithm : validAlgorithmListToExecute) {
            AlgorithmTask algorithmTask = algorithmTaskFactory.create(bundleAlgorithm.getId(), recInputParams.getCcp(), recCycleStatus);
            futures.put(bundleAlgorithm.getId(), cachedTaskExecutorService.submit(algorithmTask));
            algoIdToDisplayText.put(bundleAlgorithm.getId(),
                                    bundleAlgorithm.getCustomDisplayText() == null ?
                                            bundleAlgorithm.getDefaultDisplayText() :
                                            bundleAlgorithm.getCustomDisplayText());
        }

        if (algoCombineInfo.isEnableCombine()) {
            return getMultipleResultWhenCombinedEnabled(futures, activeBundle, recCycleStatus);
        } else {
            return getMultipleResultWhenNonCombined(futures, activeBundle, algoIdToDisplayText, recCycleStatus);
        }
    }

    /**
     * Get output from algorithms when combine enabled
     *
     * @param futures        futures to execute
     * @param activeBundle   active bundle
     * @param recCycleStatus rec cycle status
     * @return combine algorithm result
     */
    private MultipleAlgorithmResult getMultipleResultWhenCombinedEnabled(Map<String, Future<AlgorithmResult>> futures, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        String displayText = activeBundle.getAlgoCombineInfo().getCombineDisplayText();
        Set<Product> products = new LinkedHashSet<>();
        Map<String, String> algoToProductsMap = new LinkedHashMap<>();
        Map<String, String> algoToUsedCcp = new LinkedHashMap<>();

        for (Map.Entry<String, Future<AlgorithmResult>> entry : futures.entrySet()) {
            Future<AlgorithmResult> algorithmResultFuture = entry.getValue();
            String algoId = entry.getKey();
            try {
                AlgorithmResult algorithmResult = algorithmResultFuture.get();
                List<Product> recProducts = algorithmResult.getRecProducts();
                if (CollectionUtils.isNotEmpty(recProducts)) {
                    products.addAll(recProducts);
                    algoToProductsMap.put(algoId, recProducts.stream().map(Product::getProductId).collect(Collectors.joining(",")));
                    algoToUsedCcp.put(algoId, algorithmResult.getUsedCcp().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
                }
                if (products.size() >= activeBundle.getLimitToApply()) {
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Error executing algorithm at algo combine: {}, BundleId: {} ",
                             recCycleStatus.getRequestId(),
                             algoId,
                             activeBundle.getId(),
                             e);
            }
        }

        MultipleAlgorithmResult multipleAlgorithmResult = new MultipleAlgorithmResult();
        multipleAlgorithmResult.setRecProducts(new LinkedList<>(products));
        multipleAlgorithmResult.setAlgoToProductsMap(algoToProductsMap);
        multipleAlgorithmResult.setAlgoToUsedCcp(algoToUsedCcp);
        multipleAlgorithmResult.setDisplayText(displayText);
        return multipleAlgorithmResult;
    }

    /**
     * Get output from algorithms when combine is disabled
     *
     * @param futures             futures to execute
     * @param activeBundle        active bundle
     * @param algoIdToDisplayText algo to display text map
     * @param recCycleStatus      rec cycle status
     * @return combine algorithm result
     */
    private MultipleAlgorithmResult getMultipleResultWhenNonCombined(Map<String, Future<AlgorithmResult>> futures, ActiveBundle activeBundle, Map<String, String> algoIdToDisplayText, RecCycleStatus recCycleStatus) {
        String displayText = StringConstants.DEFAULT_DISPLAY_TEXT;

        Set<Product> products = new LinkedHashSet<>();
        Map<String, String> algoToProductsMap = new LinkedHashMap<>();
        Map<String, String> algoToUsedCcp = new LinkedHashMap<>();

        List<Product> maxProductList = new LinkedList<>();
        Map<String, String> maxAlgoUsedCcp = new LinkedHashMap<>();
        String maxAlgoId = null;

        for (Map.Entry<String, Future<AlgorithmResult>> entry : futures.entrySet()) {
            Future<AlgorithmResult> algorithmResultFuture = entry.getValue();
            String algoId = entry.getKey();
            try {
                AlgorithmResult algorithmResult = algorithmResultFuture.get();
                List<Product> recProducts = algorithmResult.getRecProducts();

                if (recProducts.size() >= activeBundle.getLimitToApply()) {
                    products.addAll(recProducts);
                    algoToProductsMap.put(algoId, products.stream().map(Product::getProductId).collect(Collectors.joining(",")));
                    algoToUsedCcp.put(algoId, algorithmResult.getUsedCcp().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
                    displayText = algoIdToDisplayText.get(algoId);
                    break;
                } else {
                    if (maxProductList.size() < recProducts.size()) {
                        maxAlgoId = algoId;
                        maxProductList = recProducts;
                        maxAlgoUsedCcp = algorithmResult.getUsedCcp();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Error executing algorithm at algo non combine: {}, BundleId: {} ",
                             recCycleStatus.getRequestId(),
                             algoId,
                             activeBundle.getId(),
                             e);
            }
        }

        if (CollectionUtils.isEmpty(products) && CollectionUtils.isNotEmpty(maxProductList)) {
            products.addAll(maxProductList);
            algoToProductsMap.put(maxAlgoId, maxProductList.stream().map(Product::getProductId).collect(Collectors.joining(",")));
            algoToUsedCcp.put(maxAlgoId, maxAlgoUsedCcp.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
            displayText = algoIdToDisplayText.get(maxAlgoId);
        }

        MultipleAlgorithmResult multipleAlgorithmResult = new MultipleAlgorithmResult();
        multipleAlgorithmResult.setRecProducts(new LinkedList<>(products));
        multipleAlgorithmResult.setAlgoToProductsMap(algoToProductsMap);
        multipleAlgorithmResult.setAlgoToUsedCcp(algoToUsedCcp);
        multipleAlgorithmResult.setDisplayText(displayText);
        return multipleAlgorithmResult;
    }
}
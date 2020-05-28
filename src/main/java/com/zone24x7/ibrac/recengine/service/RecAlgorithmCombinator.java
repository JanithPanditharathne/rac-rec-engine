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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
        String displayText = StringConstants.DEFAULT_DISPLAY_TEXT;

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
        Map<String,String> algoIdToDisplayText = new LinkedHashMap<>();

        //TODO: Change logic to limit HBase load by calling batch wise
        for (BundleAlgorithm bundleAlgorithm : validAlgorithmListToExecute) {
            AlgorithmTask algorithmTask = algorithmTaskFactory.create(bundleAlgorithm.getId(), recInputParams.getCcp(), recCycleStatus);
            futures.put(bundleAlgorithm.getId(), cachedTaskExecutorService.submit(algorithmTask));
            algoIdToDisplayText.put(bundleAlgorithm.getId(),
                                    bundleAlgorithm.getCustomDisplayText() == null ?
                                            bundleAlgorithm.getDefaultDisplayText() :
                                            bundleAlgorithm.getCustomDisplayText());
        }

        Integer limitToApply = activeBundle.getLimitToApply();
        List<Product> products = new LinkedList<>();

        Map<String, String> algoToProductsMap = new LinkedHashMap<>();

        //TODO: Populate used ccp
        Map<String, String> algoToUsedCcp = new LinkedHashMap<>();

        if (algoCombineInfo.isEnableCombine()) {
            //TODO: Handle single algo producing o/p scenario
            displayText = algoCombineInfo.getCombineDisplayText();
            for (Map.Entry<String, Future<AlgorithmResult>> entry : futures.entrySet()) {
                Future<AlgorithmResult> algorithmResultFuture = entry.getValue();
                String algoId = entry.getKey();
                try {
                    AlgorithmResult algorithmResult = algorithmResultFuture.get();
                    List<Product> recProducts = algorithmResult.getRecProducts();
                    if (CollectionUtils.isNotEmpty(recProducts)) {
                        products.addAll(recProducts);
                        algoToProductsMap.put(algoId, recProducts.stream().map(Product::getProductId).collect(Collectors.joining(",")));
                    }
                    if (products.size() >= limitToApply) {
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
        } else {
            List<Product> maxProductList = new LinkedList<>();
            String maxAlgoId = null;

            for (Map.Entry<String, Future<AlgorithmResult>> entry : futures.entrySet()) {
                Future<AlgorithmResult> algorithmResultFuture = entry.getValue();
                String algoId = entry.getKey();
                try {
                    AlgorithmResult algorithmResult = algorithmResultFuture.get();
                    List<Product> recProducts = algorithmResult.getRecProducts();

                    if (recProducts.size() >= limitToApply) {
                        products.addAll(recProducts);
                        algoToProductsMap.put(algoId, products.stream().map(Product::getProductId).collect(Collectors.joining(",")));
                        displayText = algoIdToDisplayText.get(algoId);
                        break;
                    } else {
                        if (maxProductList.size() < recProducts.size()) {
                            maxProductList = recProducts;
                            maxAlgoId = algoId;
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
                displayText = algoIdToDisplayText.get(maxAlgoId);
            }
        }

        MultipleAlgorithmResult multipleAlgorithmResult = new MultipleAlgorithmResult();
        multipleAlgorithmResult.setRecProducts(products);
        multipleAlgorithmResult.setAlgoToProductsMap(algoToProductsMap);
        //TODO: set algo used ccps
        // multipleAlgorithmResult.setAlgoToUsedCcp();
        multipleAlgorithmResult.setDisplayText(displayText);
        return multipleAlgorithmResult;
    }
}
package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(RecAlgorithmCombinator.class);

    @Autowired
    private AlgorithmTaskFactory algorithmTaskFactory;

    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

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

        if (CollectionUtils.isEmpty(validAlgorithmListToExecute) || algoCombineInfo == null) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Required information not available for algorithm execution and combination. " +
                                 "AlgoCombineInfo: {}, ValidAlgorithmListToExecute: {}",
                         recCycleStatus.getRequestId(),
                         algoCombineInfo,
                         validAlgorithmListToExecute);
            return new MultipleAlgorithmResult();
        }

        Map<String, Future<AlgorithmResult>> futures = new LinkedHashMap<>();
        Map<String, String> algoIdToDisplayText = new LinkedHashMap<>();

        for (BundleAlgorithm bundleAlgorithm : validAlgorithmListToExecute) {
            AlgorithmTask algorithmTask = algorithmTaskFactory.create(bundleAlgorithm.getId(), recInputParams.getCcp(), recCycleStatus);
            futures.put(bundleAlgorithm.getId(), cachedTaskExecutorService.submit(algorithmTask));
            algoIdToDisplayText.put(bundleAlgorithm.getId(),
                                    bundleAlgorithm.getCustomDisplayText() == null ?
                                            bundleAlgorithm.getDefaultDisplayText() :
                                            bundleAlgorithm.getCustomDisplayText());
        }

        if (algoCombineInfo.isEnableCombine()) {
            return getMultipleResultWhenCombinedEnabled(futures, activeBundle, algoIdToDisplayText, recCycleStatus);
        } else {
            return getMultipleResultWhenNonCombined(futures, activeBundle, algoIdToDisplayText, recCycleStatus);
        }
    }

    /**
     * Get output from algorithms when combine enabled
     *
     * @param futures             futures to execute
     * @param activeBundle        active bundle
     * @param algoIdToDisplayText algoIdToDisplayText map
     * @param recCycleStatus      rec cycle status
     * @return combine algorithm result
     */
    private MultipleAlgorithmResult getMultipleResultWhenCombinedEnabled(Map<String, Future<AlgorithmResult>> futures,
                                                                         ActiveBundle activeBundle,
                                                                         Map<String, String> algoIdToDisplayText,
                                                                         RecCycleStatus recCycleStatus) {
        String displayText = activeBundle.getAlgoCombineInfo().getCombineDisplayText();
        Set<Product> combinedProducts = new LinkedHashSet<>();
        Map<String, String> algoToProductsMap = new LinkedHashMap<>();
        Map<String, String> algoToUsedCcp = new LinkedHashMap<>();

        for (Map.Entry<String, Future<AlgorithmResult>> entry : futures.entrySet()) {
            Future<AlgorithmResult> algorithmResultFuture = entry.getValue();
            String algoId = entry.getKey();

            AlgorithmResult algorithmResult = resolveAndGetAlgoResult(algorithmResultFuture, activeBundle, algoId, recCycleStatus, "Combine");

            if (algorithmResult != null) {
                List<Product> algoProducts = algorithmResult.getRecProducts();

                if (CollectionUtils.isEmpty(combinedProducts) && CollectionUtils.isNotEmpty(algoProducts) && (algoProducts.size() >= activeBundle.getLimitToApply())) {
                    //Change display text if total result is generated by one algorithm
                    displayText = algoIdToDisplayText.get(algoId);
                }

                if (CollectionUtils.isNotEmpty(algoProducts)) {
                    combinedProducts.addAll(algoProducts);
                    algoToProductsMap.put(algoId, algoProducts.stream().map(Product::getProductId).collect(Collectors.joining(",")));
                    algoToUsedCcp.put(algoId, algorithmResult.getUsedCcp().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
                }
                if (combinedProducts.size() >= activeBundle.getLimitToApply()) {
                    break;
                }
            }
        }

        if (algoToProductsMap.size() == 1 && (combinedProducts.size() < activeBundle.getLimitToApply())) {
            displayText = algoIdToDisplayText.get(algoToProductsMap.keySet().stream().findFirst().orElse(null));
        }

        MultipleAlgorithmResult multipleAlgorithmResult = new MultipleAlgorithmResult();
        multipleAlgorithmResult.setRecProducts(new LinkedList<>(combinedProducts));
        multipleAlgorithmResult.addToAlgoToProductsMap(algoToProductsMap);
        multipleAlgorithmResult.addToAlgoToUsedCcp(algoToUsedCcp);
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
    private MultipleAlgorithmResult getMultipleResultWhenNonCombined(Map<String, Future<AlgorithmResult>> futures,
                                                                     ActiveBundle activeBundle,
                                                                     Map<String, String> algoIdToDisplayText,
                                                                     RecCycleStatus recCycleStatus) {
        String displayText = StringConstants.DEFAULT_DISPLAY_TEXT;

        Set<Product> outputProducts = new LinkedHashSet<>();
        Map<String, String> algoToProductsMap = new LinkedHashMap<>();
        Map<String, String> algoToUsedCcp = new LinkedHashMap<>();

        List<Product> maxProductList = new LinkedList<>();
        Map<String, String> maxAlgoUsedCcp = new LinkedHashMap<>();
        String maxAlgoId = null;

        for (Map.Entry<String, Future<AlgorithmResult>> entry : futures.entrySet()) {
            Future<AlgorithmResult> algorithmResultFuture = entry.getValue();
            String algoId = entry.getKey();

            AlgorithmResult algorithmResult = resolveAndGetAlgoResult(algorithmResultFuture, activeBundle, algoId, recCycleStatus, "nonCombine");

            if (algorithmResult != null) {
                List<Product> algoProducts = algorithmResult.getRecProducts();
                if (algoProducts.size() >= activeBundle.getLimitToApply()) {
                    outputProducts.addAll(algoProducts);
                    algoToProductsMap.put(algoId, outputProducts.stream().map(Product::getProductId).collect(Collectors.joining(",")));
                    algoToUsedCcp.put(algoId, algorithmResult.getUsedCcp().entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
                    displayText = algoIdToDisplayText.get(algoId);
                    break;
                } else if (maxProductList.size() < algoProducts.size()) {
                    maxAlgoId = algoId;
                    maxProductList = algoProducts;
                    maxAlgoUsedCcp = algorithmResult.getUsedCcp();
                }
            }
        }

        if (CollectionUtils.isEmpty(outputProducts) && CollectionUtils.isNotEmpty(maxProductList)) {
            outputProducts.addAll(maxProductList);
            algoToProductsMap.put(maxAlgoId, maxProductList.stream().map(Product::getProductId).collect(Collectors.joining(",")));
            algoToUsedCcp.put(maxAlgoId, maxAlgoUsedCcp.entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(",")));
            displayText = algoIdToDisplayText.get(maxAlgoId);
        }

        MultipleAlgorithmResult multipleAlgorithmResult = new MultipleAlgorithmResult();
        multipleAlgorithmResult.setRecProducts(new LinkedList<>(outputProducts));
        multipleAlgorithmResult.addToAlgoToProductsMap(algoToProductsMap);
        multipleAlgorithmResult.addToAlgoToUsedCcp(algoToUsedCcp);
        multipleAlgorithmResult.setDisplayText(displayText);
        return multipleAlgorithmResult;
    }

    /**
     * Get the result of algorithmResultFuture.
     *
     * @param algorithmResultFuture algorithmResultFuture to resolve
     * @param recCycleStatus        recCycleStatus object
     * @param algoId                algorithm id
     * @param activeBundle          active bundle
     * @return Algorithm Result
     */
    private static AlgorithmResult resolveAndGetAlgoResult(Future<AlgorithmResult> algorithmResultFuture, ActiveBundle activeBundle, String algoId, RecCycleStatus recCycleStatus, String mode) {
        try {
            return algorithmResultFuture.get();
        } catch (InterruptedException e) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Error executing algorithm. mode:{} algo:{}, BundleId:{} ",
                         recCycleStatus.getRequestId(),
                         mode,
                         algoId,
                         activeBundle.getId(),
                         e);

            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Error executing algorithm. mode:{} algo:{}, BundleId:{} ",
                         recCycleStatus.getRequestId(),
                         mode,
                         algoId,
                         activeBundle.getId(),
                         e);
        }
        return null;
    }
}

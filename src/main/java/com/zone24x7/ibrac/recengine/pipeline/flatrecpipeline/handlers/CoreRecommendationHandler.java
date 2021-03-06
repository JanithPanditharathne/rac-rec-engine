package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.service.AlgorithmCombinator;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Class to represent the core recommendations handler implementation.
 */
@Component
@Qualifier("coreRecommendationHandler")
public class CoreRecommendationHandler implements RecUnitHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreRecommendationHandler.class);

    @Autowired
    private AlgorithmCombinator algorithmCombinator;

    /**
     * Method to handle the core recommendation generation.
     *
     * @param recInputParams  the input parameters for recommendation generation
     * @param recStatusParams rec status parameters for recommendation generation
     * @param activeBundle    the activeBundle for recommendation generation
     */
    @Override
    public void handleTask(RecInputParams recInputParams, RecStatusParams recStatusParams, ActiveBundle activeBundle) {
        MultipleAlgorithmResult multipleAlgorithmResult = algorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recStatusParams.getRecCycleStatus());

        if (multipleAlgorithmResult != null) {
            String productIds = multipleAlgorithmResult.getRecProducts().stream().map(Product::getProductId).collect(Collectors.joining(","));
            LOGGER.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Multiple algo result generated. placeholder: {}, " +
                                "ProductIds:{}, algoToProductMap: {}, algoToCcpMap: {}",
                        recStatusParams.getRecCycleStatus().getRequestId(),
                        recInputParams.getPlaceholder(),
                        productIds,
                        multipleAlgorithmResult.getAlgoToProductsMap(),
                        multipleAlgorithmResult.getAlgoToUsedCcp());
        } else {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "MultipleAlgorithmResult is null for placeholder: {}",
                        recStatusParams.getRecCycleStatus().getRequestId(),
                        recInputParams.getPlaceholder());
        }
        recStatusParams.setMultipleAlgorithmResult(multipleAlgorithmResult);
    }
}

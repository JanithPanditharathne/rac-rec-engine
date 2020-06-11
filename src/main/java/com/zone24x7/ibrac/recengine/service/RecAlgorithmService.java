package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.combinationgenerator.AlgoCombinationIteratorProvider;
import com.zone24x7.ibrac.recengine.combinationgenerator.AlgorithmCombination;
import com.zone24x7.ibrac.recengine.combinationgenerator.CombinationIterator;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Responsible for executing a single recommendation algorithm.
 */
@Component
public class RecAlgorithmService implements AlgorithmService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecAlgorithmService.class);

    @Autowired
    private AlgorithmResultGenerator algorithmResultGenerator;

    @Autowired
    private AlgoCombinationIteratorProvider algoCombinationIteratorProvider;

    /**
     * Calls ccp iterators and with the given ccp calls relevant classes and retrieves the recommendations
     *
     * @param algorithmId    algorithm Id
     * @param ccp            channel context parameter
     * @param recCycleStatus recCycle status.
     * @return result generated from algorithm.
     */
    public AlgorithmResult getAlgorithmResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        AlgorithmResult algorithmResult = new AlgorithmResult();

        CombinationIterator<AlgorithmCombination> algorithmCombinationIterator = algoCombinationIteratorProvider.getCombinationIterator(algorithmId, ccp, recCycleStatus);
        StringBuilder logMessageBuilder = new StringBuilder();

        while (algorithmCombinationIterator.hasMoreCombinations()) {
            //Get the next algorithm combination
            AlgorithmCombination currentAlgorithmCombination = algorithmCombinationIterator.getNextCombination();
            Map<String, String> currentCcp = currentAlgorithmCombination.getCombinationMap();

            algorithmResult = algorithmResultGenerator.getAlgorithmResult(algorithmId, currentCcp, recCycleStatus);

            if (algorithmResult != null && CollectionUtils.isNotEmpty(algorithmResult.getRecProducts())) {
                LOGGER.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Results produced for. AlgoId: {}, ccp: {}",
                            recCycleStatus.getRequestId(), algorithmId, currentCcp);
                break;
            } else {
                logMessageBuilder.append(currentCcp.toString());
            }
        }

        String noResultCcps = logMessageBuilder.toString();
        if (StringUtils.isNotEmpty(noResultCcps)) {
            LOGGER.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "No Results produced for. AlgoId: {}, ccps: {}",
                        recCycleStatus.getRequestId(), algorithmId, noResultCcps);
        }

        return algorithmResult;
    }
}

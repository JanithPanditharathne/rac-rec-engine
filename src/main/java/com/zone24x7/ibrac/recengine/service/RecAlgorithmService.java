package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.combinationgenerator.AlgoCombinationIteratorProvider;
import com.zone24x7.ibrac.recengine.combinationgenerator.AlgorithmCombination;
import com.zone24x7.ibrac.recengine.combinationgenerator.CombinationIterator;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Responsible for executing a single recommendation algorithm.
 */
@Component
public class RecAlgorithmService implements AlgorithmService {

    @Autowired
    private AlgorithmResultGenerator algorithmResultGenerator;

    @Log
    private static Logger logger;

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

        algorithmCombinationIterator.initialize();
        while (algorithmCombinationIterator.hasMoreCombinations()) {
            //Get the next algorithm combination
            AlgorithmCombination currentAlgorithmCombination = algorithmCombinationIterator.getNextCombination();
            Map<String, String> currentCcp = currentAlgorithmCombination.getCombinationMap();

            algorithmResult = algorithmResultGenerator.getAlgorithmResult(algorithmId, currentCcp, recCycleStatus);

            if (algorithmResult != null && CollectionUtils.isNotEmpty(algorithmResult.getRecProducts())) {
                logger.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Results produced for. AlgoId: {}, ccp: {}",
                            recCycleStatus.getRequestId(), algorithmId, currentCcp);
                break;
            }
        }

        return algorithmResult;
    }
}
package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Class to provide a algorithm combination iterator.
 */
@Component
public class AlgoCombinationIteratorProvider implements CombinationIteratorProvider<CombinationIterator<AlgorithmCombination>> {

    @Autowired
    private ApplicationContext context;

    /**
     * Provides a algorithm combination iterator for a given algo id and input ccp map.
     *
     * @param algoId         Id of the algorithm.
     * @param ccp            Map of ccp params
     * @param recCycleStatus recommendation generation cycle status
     * @return {@link CombinationIterator}
     */
    @Override
    public CombinationIterator<AlgorithmCombination> getCombinationIterator(String algoId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        CombinationIterator<AlgorithmCombination> iterator = context.getBean(AlgorithmCombinationIterator.class, algoId, ccp, recCycleStatus);
        iterator.initialize();

        return iterator;
    }
}

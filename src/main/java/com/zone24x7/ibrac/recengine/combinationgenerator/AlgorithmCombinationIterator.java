package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Implementation of the Algorithm Combination Iterator
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlgorithmCombinationIterator implements CombinationIterator<AlgorithmCombination> {
    private List<List<String>> combinationLists;
    private List<String> nextCombination;
    private int currentCombinationIndex;

    private Map<String, String> originalCcpMap;
    private String algoId;
    private String requestId;

    @Autowired
    @Qualifier("algoParamsMap")
    private Map<String, AlgoParams> algoParamsMap;

    @Autowired
    private AlgorithmCombinationGenerator algorithmCombinationGenerator;

    /**
     * Constructor of AlgorithmCombinationIterator.
     *
     * @param algoId         id of the algorithm.
     * @param ccp            input ccp params map.
     * @param recCycleStatus rec cycle status.
     */
    AlgorithmCombinationIterator(String algoId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        this.algoId = algoId;
        this.originalCcpMap = ccp;
        this.requestId = recCycleStatus.getRequestId();
    }

    /**
     * Initializes the iterator by generating algo combination lists.
     */
    @Override
    public void initialize() {
        // Gets the algo params for the given algoId.
        AlgoParams algoParams = algoParamsMap.get(algoId);

        // Generates possible list of combinations.
        combinationLists = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, originalCcpMap, requestId);
        // Set the first combination as next combination.
        if (CollectionUtils.isNotEmpty(combinationLists)) {
            nextCombination = combinationLists.get(0);
        }
    }

    /**
     * Method to check if iterator has more combinations.
     *
     * @return true if next combination is not null.
     */
    @Override
    public boolean hasMoreCombinations() {
        return nextCombination != null;
    }

    /**
     * Method to provide the next {@link AlgorithmCombination}
     *
     * @return next algorithmCombination.
     */
    @Override
    public AlgorithmCombination getNextCombination() {
        if (nextCombination == null) {
            throw new NoSuchElementException("No more combinations left");
        }

        //use the currently assigned next combination and create combination map.
        AlgorithmCombination algorithmCombination = generateCombination();
        currentCombinationIndex++;

        if (combinationLists.size() > currentCombinationIndex) {
            // set the next combination list.
            nextCombination = combinationLists.get(currentCombinationIndex);
        } else {
            nextCombination = null;
        }

        return algorithmCombination;
    }

    /**
     * Generates ccp combination using next list of algo param combination.
     *
     * @return ccp combination.
     */
    private AlgorithmCombination generateCombination() {
        Map<String, String> combinationMap = new LinkedHashMap<>();
        for (String combinationParam : nextCombination) {
            combinationMap.put(combinationParam, originalCcpMap.get(combinationParam));
        }

        return new AlgorithmCombination(combinationMap);
    }

    /**
     * Clears the current combination list.
     */
    @Override
    public void dispose() {
        if (CollectionUtils.isNotEmpty(combinationLists)) {
            combinationLists.clear();
            nextCombination = null;
        }
    }
}

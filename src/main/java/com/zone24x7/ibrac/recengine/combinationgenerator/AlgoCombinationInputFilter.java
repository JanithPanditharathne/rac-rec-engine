package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to filter ccp input based on algo params.
 */
@Component
class AlgoCombinationInputFilter {

    /**
     * Method to provide the ccp mandatory params that is required by the algorithm.
     *
     * @param algoParams algorithm params.
     * @param ccpMap     input ccp map.
     * @return List of filtered mandatory params.
     */
    List<String> getMandatoryParams(AlgoParams algoParams, Map<String, String> ccpMap) {
        List<String> algoMandatoryParams = algoParams.getMandatoryParams();

        //check for all the mandatory params
        if (CollectionUtils.isNotEmpty(algoMandatoryParams) && ccpMap.keySet().containsAll(algoMandatoryParams)) {
            return new LinkedList<>(algoMandatoryParams);
        }

        return Collections.emptyList();
    }

    /**
     * Method to provide the ccp conditional mandatory algo params required by the algorithm.
     *
     * @param algoParams algorithm params.
     * @param ccpMap     input ccp map.
     * @return List of filtered conditional mandatory params.
     */
    List<List<String>> getConditionalMandatoryParams(AlgoParams algoParams, Map<String, String> ccpMap) {
        List<List<String>> algoConditionalMandatoryParams = algoParams.getConditionalMandatoryParams();

        if (CollectionUtils.isNotEmpty(algoConditionalMandatoryParams)) {
            return algoConditionalMandatoryParams.stream()
                                                 .filter(combination -> ccpMap.keySet().containsAll(combination))
                                                 .collect(Collectors.toList());

        }

        return Collections.emptyList();
    }

    /**
     * Method to provide the ccp optional algo params that is required by the algorithm.
     *
     * @param algoParams algorithm params.
     * @param ccpMap     input ccp map.
     * @return List of filtered optional params.
     */
    List<String> getOptionalParams(AlgoParams algoParams, Map<String, String> ccpMap) {
        List<String> algoOptionalParams = algoParams.getOptionalParams();

        //check for the available optional params
        if (CollectionUtils.isNotEmpty(algoOptionalParams)) {
            List<String> intersectedList = new LinkedList<>();
            algoOptionalParams.stream().filter(ccpMap::containsKey).collect(Collectors.toCollection(() -> intersectedList));

            return intersectedList;
        }

        return Collections.emptyList();
    }
}

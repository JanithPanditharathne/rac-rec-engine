package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Class to to generate  CCP parameter combinations to a given algorithm.
 */
@Component
public class AlgorithmCombinationGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlgorithmCombinationGenerator.class);

    private static final String ALGO_PARAMS_NULL_MSG = "Algo params to generate algorithm combinations is null.";
    private static final String EMPTY_CCP_MAP_TO_GENERATE_ALGO_COMBINATIONS_MSG = "Empty ccp map to generate algorithm combinations. Algo Id: {}";
    private static final String MANDATORY_PARAMS_NOT_FOUND_MSG = "Mandatory params not found in ccp map to generate algorithm combinations. AlgoId: {} Ccp Map: {} Required Mandatory Params: {}";
    private static final String CONDITIONAL_MANDATORY_PARAMS_NOT_FOUND_MSG = "Conditional mandatory params not found in ccp map to generate algorithm combinations. AlgoId: {} Ccp Map: {} Required Mandatory Params: {}";
    private static final String NO_COMBINATIONS_GENERATED_MSG = "No algorithm combinations generated. Algo Id: {} Ccp Map: {}";

    @Autowired
    private AlgoCombinationInputFilter algoCombinationInputFilter;

    /**
     * Method to generate combinations based on algorithm params and input ccp params.
     *
     * @param algoParams algorithm params to consider.
     * @param ccp        input ccp params.
     * @param requestId  id of the request.
     * @return List of valid combinations.
     */
    List<List<String>> generateAlgoCombinations(AlgoParams algoParams, Map<String, String> ccp, String requestId) {
        // if algo params is null, return an empty list.
        if (algoParams == null) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + ALGO_PARAMS_NULL_MSG, requestId);
            return Collections.emptyList();
        }

        // if the algorithm is a generic algo which doesnt required ccp, return a list of empty list.
        if (isGenericAlgorithm(algoParams)) {
            return Collections.singletonList(Collections.emptyList());
        }

        if (MapUtils.isEmpty(ccp)) {
            LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EMPTY_CCP_MAP_TO_GENERATE_ALGO_COMBINATIONS_MSG, requestId, algoParams.getAlgoId());
            return Collections.emptyList();
        }

        // filter input ccp params using the algo params to generate combinations.
        List<String> mandatoryParams = algoCombinationInputFilter.getMandatoryParams(algoParams, ccp);
        List<List<String>> conditionalMandatoryParams = algoCombinationInputFilter.getConditionalMandatoryParams(algoParams, ccp);
        List<String> optionalParams = algoCombinationInputFilter.getOptionalParams(algoParams, ccp);
        boolean optionalCombinationEnabled = algoParams.isOptionalCombEnabled();

        // if algo params has mandatory params, get mandatory param based combinations.
        if (CollectionUtils.isNotEmpty(algoParams.getMandatoryParams())) {
            // return an empty list, since no mandatory params found.
            if (CollectionUtils.isEmpty(mandatoryParams)) {
                LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + MANDATORY_PARAMS_NOT_FOUND_MSG, requestId, algoParams.getAlgoId(), ccp, algoParams.getMandatoryParams());
                return Collections.emptyList();
            } else {
                return generateMandatoryCombinations(algoParams, mandatoryParams, conditionalMandatoryParams, optionalParams);
            }
        }

        // Move to conditional mandatory param combinations
        if (CollectionUtils.isNotEmpty(algoParams.getConditionalMandatoryParams())) {
            // return an empty list, if conditional mandatory params are empty.
            if (CollectionUtils.isEmpty(conditionalMandatoryParams)) {
                LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + CONDITIONAL_MANDATORY_PARAMS_NOT_FOUND_MSG, requestId, algoParams.getAlgoId(), ccp, algoParams.getConditionalMandatoryParams());
                return Collections.emptyList();
            }

            if (CollectionUtils.isNotEmpty(optionalParams)) {
                return getConditionalMandatoryAndOptionalParams(conditionalMandatoryParams, optionalParams, optionalCombinationEnabled);
            } else {
                // conditional mandatory only combinations
                return conditionalMandatoryParams;
            }
        }

        // Get optional only combinations.
        if (CollectionUtils.isNotEmpty(optionalParams)) {
            return getOptionalCombinations(optionalParams, optionalCombinationEnabled);
        }

        LOGGER.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + NO_COMBINATIONS_GENERATED_MSG, requestId, algoParams.getAlgoId(), ccp);
        return Collections.emptyList();
    }

    /**
     * Method to get optional param combinations.
     *
     * @param optionalParams             list of optional params.
     * @param optionalCombinationEnabled if optional combinations is enabled.
     * @return list of optional combinations.
     */
    private List<List<String>> getOptionalCombinations(List<String> optionalParams, boolean optionalCombinationEnabled) {
        // if combinations are enabled, combine param lists.
        if (optionalCombinationEnabled) {
            return getOptionalParamSubSets(optionalParams);
        } else {
            List<List<String>> finalCombinationList = new LinkedList<>();
            optionalParams.forEach(optionalParam -> {
                List<String> params = new LinkedList<>();
                params.add(optionalParam);
                finalCombinationList.add(params);
            });

            return finalCombinationList;
        }
    }

    /**
     * Method to generate mandatory params based combinations.
     *
     * @param algoParams                 algorithm based algo params.
     * @param mandatoryParams            filtered mandatory params
     * @param conditionalMandatoryParams filtered conditional mandatory params
     * @param optionalParams             filtered optional params.
     * @return List of combinations.
     */
    private List<List<String>> generateMandatoryCombinations(AlgoParams algoParams, List<String> mandatoryParams, List<List<String>> conditionalMandatoryParams, List<String> optionalParams) {
        // mandatory + conditionally mandatory params combinations
        if (CollectionUtils.isNotEmpty(conditionalMandatoryParams)) {
            List<List<String>> combinationList = new LinkedList<>(Collections.singletonList(mandatoryParams));
            combinationList.addAll(conditionalMandatoryParams);
            return combinationList;
        }

        // mandatory + optional params combinations
        if (CollectionUtils.isNotEmpty(optionalParams)) {
            return getMandatoryAndOptionalCombinations(mandatoryParams, optionalParams, algoParams.isOptionalCombEnabled());
        }

        // mandatory only combination
        return Collections.singletonList(mandatoryParams);
    }

    /**
     * Method to generate mandatory and optional combinations.
     * <p>
     * Input: Mandatory[M1, M2] optional [Op1, Op2]
     * Output : [[M1, M2, Op1, Op2], [M1, M2, Op1], [M1, M2, Op2], [M1, M2]]
     *
     * @param mandatoryParams     mandatory params.
     * @param optionalParams      optional params.
     * @param optionalCombEnabled enable optional combinations. If this is false, combinations will not be generated.
     * @return list of combinations.
     */
    private List<List<String>> getMandatoryAndOptionalCombinations(List<String> mandatoryParams, List<String> optionalParams, boolean optionalCombEnabled) {
        // if combinations are enabled, combine param lists.
        if (optionalCombEnabled) {
            List<List<String>> optionalParamSubset = getOptionalParamSubSets(optionalParams);
            return generateOptionalParamsBasedCombinations(Collections.singletonList(mandatoryParams), optionalParamSubset);
        } else {
            return getCombinationsDisabledOptionalParams(mandatoryParams, optionalParams);
        }
    }

    /**
     * Method to generate conditional mandatory and optional combinations.
     * Permutations of optional combinations will be combined with conditional mandatory param combination.
     *
     * @param conMandatoryParams         conditional mandatory params.
     * @param optionalParams             optional params.
     * @param optionalCombinationEnabled enable optional combinations. If this is false, combinations will not be generated.
     * @return list of combinations.
     */
    private List<List<String>> getConditionalMandatoryAndOptionalParams(List<List<String>> conMandatoryParams, List<String> optionalParams, boolean optionalCombinationEnabled) {
        // if combinations are enabled, combine param lists.
        if (optionalCombinationEnabled) {
            List<List<String>> optionalParamSubset = getOptionalParamSubSets(optionalParams);
            return generateOptionalParamsBasedCombinations(conMandatoryParams, optionalParamSubset);
        } else {
            List<List<String>> mandatoryAndOptionalParams = new LinkedList<>();
            for (List<String> conditionalMandatoryList : conMandatoryParams) {
                mandatoryAndOptionalParams.addAll(getCombinationsDisabledOptionalParams(conditionalMandatoryList, optionalParams));
            }

            return mandatoryAndOptionalParams;
        }
    }

    /**
     * Generates optional params based combinations.
     *
     * @param listToCombineWith list to combine optional params with.
     * @param optionalParams    list of optional params.
     * @return optional and main combination lists.
     */
    private static List<List<String>> generateOptionalParamsBasedCombinations(List<List<String>> listToCombineWith, List<List<String>> optionalParams) {
        List<List<String>> finalList = new LinkedList<>();
        // for each mandatory/conditional mandatory params, combine with optional params.
        for (List<String> list : listToCombineWith) {
            // iterate optional params and combine with each list.
            for (List<String> optionalList : optionalParams) {
                List<String> combinedList = new LinkedList<>(list);
                combinedList.addAll(optionalList);
                finalList.add(combinedList);
            }
            //add the list as the final combination.
            finalList.add(list);
        }

        return finalList;
    }

    /**
     * Method to generate subsets of a given list.
     *
     * @param optionalParamsList list to generate subsets.
     * @return List of generated subsets.
     */
    private List<List<String>> getOptionalParamSubSets(List<String> optionalParamsList) {
        List<List<String>> optionalParamSubSets = new LinkedList<>();
        int size = optionalParamsList.size();
        for (int i = size; i > 0; i--) {
            Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(size, i);
            while (iterator.hasNext()) {
                final int[] combination = iterator.next();
                List<String> paramSubSetList = new LinkedList<>();

                for (int c : combination) {
                    paramSubSetList.add(optionalParamsList.get(c));
                }

                optionalParamSubSets.add(paramSubSetList);
            }
        }

        return optionalParamSubSets;
    }

    /**
     * Method to generate optional params when optional param combination is disabled.
     *
     * @param params         list of params to combine with.
     * @param optionalParams list of optional params.
     * @return list of combined params.
     */
    private static List<List<String>> getCombinationsDisabledOptionalParams(List<String> params, List<String> optionalParams) {
        List<List<String>> finalCombinationList = new LinkedList<>();
        optionalParams.forEach(optionalParam -> {
            List<String> combinedParams = new LinkedList<>(params);
            combinedParams.add(optionalParam);
            finalCombinationList.add(combinedParams);
        });

        finalCombinationList.add(params);

        return finalCombinationList;
    }

    /**
     * Checks whether the given algo is a generic algorithm.
     *
     * @param algoParams algorithm params.
     * @return true if algo is a generic algorithm.
     */
    private boolean isGenericAlgorithm(AlgoParams algoParams) {
        return (CollectionUtils.isEmpty(algoParams.getMandatoryParams()) &&
                CollectionUtils.isEmpty(algoParams.getConditionalMandatoryParams()) &&
                CollectionUtils.isEmpty(algoParams.getOptionalParams()));
    }
}
